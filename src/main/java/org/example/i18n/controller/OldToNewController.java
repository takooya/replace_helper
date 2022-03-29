package org.example.i18n.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.example.i18n.domain.param.LoopFile2MapParam;
import org.example.i18n.domain.dto.LoopFileParam;
import org.example.i18n.domain.bases.ReplaceInfo;
import org.example.i18n.temp.MainUtil;
import org.example.i18n.utils.DirectoryUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping
@SuppressWarnings({"java:S112", "AlibabaLowerCamelCaseVariableNaming"})
public class OldToNewController {

    /**
     * 资源文件夹下所有文件是否包含关键字,包含的文件行按一定规则持久化到另一个临时文件
     *
     * @param param    资源信息,关键字信息,类型过滤信息等
     * @param response 响应结果
     * @throws IOException 文件io异常
     */
    @PostMapping("/old2new")
    public void old2new(@RequestBody LoopFileParam param, HttpServletResponse response) throws IOException {
        File target = File.createTempFile("temp", ".txt");
        // 检查资源路径,并获取对应资源路径的File对象
        File sources = MainUtil.checkAndGetDir(param.getSourcesPath());
        // 设置文件遍历参数
        LoopFileParam<Set<String>> loopParam = new LoopFileParam<Set<String>>()
                .setSources(sources)
                .setTarget(target)
                .setData(new HashSet<>());
        DirectoryUtil.loopFiles(loopParam, loopFileParam -> {
            // 上述资源路径下的某一个文件
            File source = loopFileParam.getSources();
            // 判断文件是否为过滤类型,如果是过滤类型,跳过文件收集
            boolean pass = loopFileParam.pass(source);
            if (pass) {
                return;
            }
            List<String> info = FileUtil.readLines(source, StandardCharsets.UTF_8);
            // 获取过滤关键字
            String keyWord = loopFileParam.getKeyWords();
            String[] keyWords = keyWord.split(",");
            for (String row : info) {
                String trimRow = row.trim();
                if (StrUtil.isBlankOrUndefined(trimRow)) {
                    continue;
                }
                if (trimRow.startsWith("//")) {
                    continue;
                }
                int i = trimRow.indexOf("//");
                if (i != -1) {
                    trimRow = trimRow.substring(0, i);
                }
                String finalTrimRow = trimRow;
                // 当前行是否包含关键字
                boolean matched = Arrays.stream(keyWords)
                        .filter(s -> !StrUtil.isBlankOrUndefined(s))
                        .anyMatch(s -> StrUtil.containsIgnoreCase(finalTrimRow, s.trim()));
                if (matched) {
                    FileUtil.appendUtf8String(source.getName() + "=" + row + "\n", target);
                }
            }
        });
        // 生成响应文件
        response.addHeader("Content-Disposition", "attachment; filename=" + target.getName());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(new Tika().detect(target.getName()));
        response.setContentLengthLong(target.length());
        IoUtil.copy(FileUtil.getInputStream(target), response.getOutputStream(), (int) target.length());
        target.deleteOnExit();
    }

    /**
     * 根据正则修改文件
     *
     * @param param 资源信息,正则信息,类型过滤信息等
     * @return 更新情况
     */
    @PostMapping("/updateByReg")
    @ResponseBody
    public List<ReplaceInfo> updateXml(@Valid @RequestBody LoopFile2MapParam param) {
        // 获取资源路径
        String sourcesPath = param.getSourcesPath();
        // 获取资源文件夹
        File sources = MainUtil.checkAndGetFile(sourcesPath);
        param.setSources(sources);

        DirectoryUtil.loopFiles2Map(param, loopFile2MapParamConsumer());
        return param.getReplaceInfos();
    }

    public static Consumer<LoopFile2MapParam> loopFile2MapParamConsumer() {
        return allInfo -> {
            // 匹配的目录下某一个文件
            File source = allInfo.getSources();
            // 1# 判断文件是否需要类型过滤
            boolean pass = allInfo.pass(source);
            if (pass) {
                return;
            }
            List<String> info = FileUtil.readLines(source, StandardCharsets.UTF_8);
            List<String> afterUpdateAll = new ArrayList<>();
            for (String row : info) {
                String result;
                String trimRow = row.trim();
                if (StrUtil.isBlankOrUndefined(trimRow)) {
                    afterUpdateAll.add(row);
                    continue;
                }
                if (trimRow.startsWith("//")) {
                    afterUpdateAll.add(row);
                    continue;
                }
                int i = trimRow.indexOf("//");
                String comment = null;
                if (i != -1) {
                    String tempRow = trimRow;
                    trimRow = tempRow.substring(0, i);
                    comment = tempRow.substring(i);
                }
                String finalTrimRow = trimRow;
                // 2# regExpMap
                // 判断key(正则表达式)是否匹配,如果匹配,按value替换
                Map<String, String> regExpMap = allInfo.getRegExpMap();
                Optional<String> any = regExpMap.keySet().stream().filter(s -> {
                    Pattern compile = Pattern.compile(s);
                    Matcher matcher = compile.matcher(finalTrimRow);
                    return matcher.find();
                }).findAny();
                if (any.isPresent()) {
                    result = ReUtil.replaceAll(trimRow, any.get(), regExpMap.get(any.get()));
                    result = comment == null ? result : result + comment;
                    allInfo.addReplaceInfo(source.getAbsolutePath(), row, result);
                } else {
                    result = row;
                }
                afterUpdateAll.add(result);
            }
            if (!allInfo.isCheck()) {
                // 覆盖模式
                FileUtil.writeLines(afterUpdateAll, source, StandardCharsets.UTF_8);
            }
        };
    }
}
