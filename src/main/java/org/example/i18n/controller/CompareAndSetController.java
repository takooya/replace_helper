package org.example.i18n.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.i18n.domain.bases.ReplaceInfo;
import org.example.i18n.domain.param.LoopFileSimpleParam;
import org.example.i18n.exceptions.JumpOutException;
import org.example.i18n.utils.DirectoryUtil;
import org.example.i18n.utils.SimilarStringUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/cas")
public class CompareAndSetController {

    /**
     * 添加注释
     *
     * @param param 循环文件工具类
     * @return 修改位置与内容 或 修改条数
     */
    @RequestMapping("/addCommend")
    public List<ReplaceInfo> addCommend(@Valid @RequestBody LoopFileSimpleParam param) {
        // 获取资源路径
        String sourceDir = param.getSourcesPath();
        File sources = new File(sourceDir);
        // 获取目标资源路径
        String targetDir = param.getTargetsPath();
        AtomicInteger changeCount = new AtomicInteger();
        // 记录结果
        DirectoryUtil.loopFiles(sources, file -> {
            boolean pass = param.pass(file);
            if (!pass) {
                return;
            }
            // 获取源码路径
            String javaPath = file.getAbsolutePath();
            if (!javaPath.contains(sourceDir)) {
                throw new RuntimeException("程序逻辑错误!");
            }
            // 获取字节码转源码路径
            String classPath = javaPath.replace(sourceDir, targetDir);
            // 判断字节码文件是否存在
            boolean exist = FileUtil.exist(classPath);
            if (!exist) {
                param.addReplaceInfo(classPath, "寻找源码对应的反编译文件没有找到!");
                return;
            }
            List<String> javaLines = FileUtil.readLines(javaPath, StandardCharsets.UTF_8);
            Map<String, List<String>> line2commend = new HashMap<>();
            List<String> temp = new ArrayList<>();
            boolean isLineCommend;
            boolean isBlockCommend = false;
            for (String javaLine : javaLines) {
                isLineCommend = javaLine.trim().startsWith("//");
                if (javaLine.trim().startsWith("/*")) {
                    isBlockCommend = true;
                }
                if (isLineCommend || isBlockCommend) {
                    temp.add(javaLine);
                }
                if (!isLineCommend && !isBlockCommend && CollUtil.isNotEmpty(temp)) {
                    line2commend.put(javaLine, new ArrayList<>(temp));
                    temp = new ArrayList<>();
                }
                if (javaLine.trim().endsWith("*/")) {
                    isBlockCommend = false;
                }
            }
            Set<String> javaLineTargetSet = line2commend.keySet();
            if (CollUtil.isNotEmpty(line2commend)) {
                log.info("[-CompareAndSetController:addCommend-]line2commend:{}", line2commend);
            }
            List<String> classLines = FileUtil.readLines(classPath, StandardCharsets.UTF_8);
            classLines = classLines.stream().map(classLine -> {
                for (String tarLine : javaLineTargetSet) {
                    // 获取语句相似度
                    float ratio = SimilarStringUtil.getSimilarityRatio(tarLine.trim(), classLine.trim());
                    if (ratio > 95.0f) {
                        List<String> commends = line2commend.get(tarLine);
                        String commendsString = String.join("\n", commends);
                        param.addReplaceInfo(classPath, classLine, commendsString);
                        return commendsString + "\n" + classLine;
                    }
                }
                return classLine;
            }).collect(Collectors.toList());
            if (!param.isCheck()) {
                FileUtil.writeLines(classLines, classPath, StandardCharsets.UTF_8);
            }
            changeCount.getAndIncrement();
        });
        if (param.isCheck()) {
            return param.getReplaceInfos();
        } else {
            throw new JumpOutException("添加注释完成,共添加" + changeCount.get() + "次.", 200);
        }
    }
}
