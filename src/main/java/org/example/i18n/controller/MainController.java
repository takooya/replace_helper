package org.example.i18n.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.example.i18n.domain.dto.LoopFileParam;
import org.example.i18n.domain.entity.DictInfo;
import org.example.i18n.domain.entity.PropertiesMap;
import org.example.i18n.domain.entity.WriteResult;
import org.example.i18n.domain.vo.CommonVo;
import org.example.i18n.mapper.DictInfoMapper;
import org.example.i18n.mapper.PropertiesMapMapper;
import org.example.i18n.mapper.WriteResultMapper;
import org.example.i18n.temp.MainUtil;
import org.example.i18n.utils.DirectoryUtil;
import org.example.i18n.utils.SimilarStringUtil;
//import org.example.i18n.utils.TestUtilKt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static org.example.i18n.consts.CommonConstant.*;

/**
 * 主接口
 *
 * @author wangyichun
 * @since 2021/12/29 7:58
 */
@Slf4j
@Controller
@RequestMapping
@SuppressWarnings({"java:S112", "AlibabaLowerCamelCaseVariableNaming"})
public class MainController {
    @Autowired
    private PropertiesMapMapper propertiesMapMapper;
    @Autowired
    private DictInfoMapper dictInfoMapper;
    @Autowired
    private WriteResultMapper writeResultMapper;

    @PostMapping("/initProp")
    public void initProp(@RequestParam Map<String, String> param, HttpServletResponse response) throws IOException {
        File target = File.createTempFile("temp", ".properties");
        String sourcePath = param.get(PROJECT_SOURCE_PATH_KEY);
        // 获取资源文件夹
        File source = MainUtil.checkAndGetDir(sourcePath);
        // 操作记录保存
        List<DictInfo> sourcePaths = dictInfoMapper.selectList(new LambdaQueryWrapper<DictInfo>()
                .eq(DictInfo::getParentCode, PROJECT_SOURCE_PATH_KEY));
        String code = CollUtil.isEmpty(sourcePaths) ? "0" : String.valueOf(sourcePaths.size());
        dictInfoMapper.insert(new DictInfo()
                .setCode(code)
                .setParentCode(PROJECT_SOURCE_PATH_KEY)
                .setValue(sourcePath));
        // 生成Properties资源文件
        LoopFileParam<Set<String>> loopParam = new LoopFileParam<Set<String>>()
                .setSources(source)
                .setTarget(target)
                .setData(new HashSet<>());
        DirectoryUtil.loopFiles(loopParam, MainUtil::generateProperties);

        // 删除数据库元数据
        propertiesMapMapper.delete(new LambdaQueryWrapper<PropertiesMap>()
                .eq(PropertiesMap::getFilename, sourcePath));

        // 将生成的数据存入数据库
        List<String> propLines = FileUtil.readLines(target, StandardCharsets.UTF_8);
        propLines.forEach(propLine -> {
            String[] split = propLine.split("=");
            String value = Arrays.stream(split).filter(s -> !Objects.equals(s, split[0])).collect(Collectors.joining("="));
            PropertiesMap pmap = new PropertiesMap().setPkey(split[0]).setValue(value).setFilename(sourcePath);
            try {
                propertiesMapMapper.insert(pmap);
            } catch (Exception e) {
                e.printStackTrace();
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

    @GetMapping("/readProp")
    @ResponseBody
    public List<PropertiesMap> readProp() {
        String sourcePath = this.getDefault().getValue();
        if (CharSequenceUtil.isBlank(sourcePath)) {
            throw new RuntimeException("请先下载properties文件!");
        }
        File source = new File(sourcePath);
        if (!source.isDirectory() || !source.exists()) {
            throw new RuntimeException("路径错误!");
        }
        return propertiesMapMapper.selectList(new LambdaQueryWrapper<PropertiesMap>()
                .eq(PropertiesMap::getFilename, sourcePath));
    }

    @PostMapping("/write")
    @ResponseBody
    public Map<String, List<WriteResult>> write(@RequestPart("file") MultipartFile prop, @RequestParam String sourcePath) throws IOException {
        String extName = FileUtil.extName(prop.getOriginalFilename());
        if (CharSequenceUtil.isBlankOrUndefined(extName) || !extName.contains("properties")) {
            throw new RuntimeException("不支持的文件类型，仅支持properties文件");
        }
        // 中文为key，properites的key为值的map
        Map<String, String> chinakeyProp = MainUtil.getChinakeyProp(prop);
        // 获取目标地址
        File source = MainUtil.checkAndGetDir(sourcePath);

        // 删除数据库记录
        int delete = writeResultMapper.delete(new LambdaQueryWrapper<WriteResult>()
                .eq(WriteResult::getPath, sourcePath));
        log.info("[-MainController-].write:删除历史记录{}条", delete);

        List<WriteResult> cannotDealLine = new ArrayList<>();
        List<WriteResult> dealedLine = new ArrayList<>();
        // 写文件
        DirectoryUtil.loopFiles(source, file -> {
            List<String> readLines = FileUtil.readLines(file, StandardCharsets.UTF_8);
            List<String> targetLines = readLines.stream().map(line -> {
                if (line.trim().startsWith("*")) {
                    return line;
                }
                Matcher matcher = chineseLP.matcher(line);
                if (!matcher.find()) {
                    return line;
                }
                Set<String> chineseKeys = new HashSet<>(chinakeyProp.keySet());
                Set<String> temp = new HashSet<>(Arrays.asList(line.split("\"")));
                chineseKeys.retainAll(temp);
                if (CollUtil.isNotEmpty(chineseKeys)) {
                    if (chineseKeys.size() > 1) {
                        WriteResult record = new WriteResult()
                                .setContent(line)
                                .setFilePath(file.getAbsolutePath())
                                .setComment("出现多次中文")
                                .setPath(sourcePath);
                        cannotDealLine.add(record);
                        writeResultMapper.insert(record);
                    } else {
                        String targetCn = chineseKeys.iterator().next();
                        String targetPkey = chinakeyProp.get(targetCn);
                        if (CharSequenceUtil.isNotBlank(targetPkey)) {
                            matcher = spaceSP.matcher(line);
                            // 获得语句缩进量
                            String space = "";
                            if (matcher.find()) {
                                space = matcher.group(1);
                            }
                            WriteResult record = new WriteResult()
                                    .setContent(line)
                                    .setFilePath(file.getAbsolutePath())
                                    .setComment("处理完成")
                                    .setPath(sourcePath);
                            dealedLine.add(record);
                            writeResultMapper.insert(record);
                            line = space + "// " + targetCn + "\n" + space +
                                    "String message = i18nUtil.getMessage(\"" + targetPkey + "\");\n" +
                                    line.replace("\"" + targetCn + "\"", "message");
                        }
                    }
                }
                return line;
            }).collect(Collectors.toList());
            FileUtil.writeLines(targetLines, file, StandardCharsets.UTF_8);
        });
        return MapUtil.builder("cannotDealLine", cannotDealLine).put("dealedLine", dealedLine).build();
    }

    @PostMapping("/writeImportAndAutowired")
    @ResponseBody
    public CommonVo<Set<String>> writeImportAndAutowired(@RequestBody String sourcePath) {
        List<WriteResult> writeResults = writeResultMapper.selectList(new LambdaQueryWrapper<WriteResult>()
                .eq(WriteResult::getPath, sourcePath));
        Set<String> markFiles = writeResults.stream()
                .map(WriteResult::getFilePath)
                .collect(Collectors.toSet());
        MainUtil.importAndAutowiredAppend(markFiles);
        return new CommonVo<Set<String>>().setData(markFiles);
    }

    @GetMapping("/getDefault")
    @ResponseBody
    public DictInfo getDefault() {
        List<DictInfo> sourcePath = dictInfoMapper.selectList(new LambdaQueryWrapper<DictInfo>()
                .eq(DictInfo::getParentCode, PROJECT_SOURCE_PATH_KEY));
        if (CollUtil.isEmpty(sourcePath)) {
            throw new RuntimeException("请输入目标文件夹并下载对应的解析文件！");
        }
        return sourcePath.stream().max((o1, o2) -> {
            DateTime t1 = DateUtil.parse(o1.getCreateTime(), TIME_FORMAT);
            DateTime t2 = DateUtil.parse(o2.getCreateTime(), TIME_FORMAT);
            return DateUtil.compare(t1, t2);
        }).orElse(new DictInfo());
    }

    @PostMapping("/checkImportAndAutowired")
    @ResponseBody
    public List<String> checkImportAndAutowired(@RequestBody Map<String, String> param) throws IOException {
        File target = File.createTempFile("temp", ".txt");
        log.info("[-MainController-].checkI18nUtil:临时文件绝对路径={}", target.getAbsolutePath());
        String sourcePath = param.get(PROJECT_SOURCE_PATH_KEY);
        File source = new File(sourcePath);
        if (!source.isDirectory()) {
            throw new RuntimeException("路径错误!");
        }
        DirectoryUtil.loopFiles(source, target, (file, file2) -> {
            final boolean[] imported = {false};
            final boolean[] autowiredMark = {false};
            final boolean[] autowired = {false};
            final boolean[] contexted = {false};
            final boolean[] isbean = {false};
            final boolean[] focus = {false};
            List<String> lines = FileUtil.readLines(file, StandardCharsets.UTF_8);
            lines.forEach(s -> {
                boolean controller = s.contains("@") && s.contains("Controller");
                boolean component = s.contains("@Component");
                boolean service = s.contains("@Service");
                boolean mapper = s.contains("@Mapper");
                boolean repository = s.contains("@Repository");
                if (controller || component || service || mapper || repository) {
                    isbean[0] = true;
                    return;
                }
                if (s.startsWith("import")) {
                    if (s.contains("com.qm.tds.util.I18nUtil")) {
                        imported[0] = true;
                    }
                    return;
                }
                if (autowiredMark[0]) {
                    if (s.contains("I18nUtil")) {
                        autowired[0] = true;
                    }
                    autowiredMark[0] = false;
                    return;
                }
                if (s.contains("@Autowired")) {
                    autowiredMark[0] = true;
                    return;
                }
                if (s.contains("SpringContextHolder.getBean(I18nUtil.class)")) {
                    contexted[0] = true;
                    return;
                }
                if (s.contains("i18nUtil") && s.contains("getMessage")) {
                    focus[0] = true;
                }
            });
            if (focus[0] && !imported[0]) {
                FileUtil.appendUtf8String(
                        file.getAbsolutePath() + ":#:" +
                                "imported:" + imported[0] + ";" +
                                "autowiredMark:" + autowiredMark[0] + ";" +
                                "autowired:" + autowired[0] + ";" +
                                "contexted:" + contexted[0] + ";" +
                                "isbean:" + isbean[0] + ";\n"
                        , target);
            }
        });
        return FileUtil.readLines(target, StandardCharsets.UTF_8);
    }

    @PostMapping("same")
    @ResponseBody
    public float same(@RequestBody Map<String, String> param) {
        String str1 = param.get("str1");
        String str2 = param.get("str2");
        return SimilarStringUtil.getSimilarityRatio(str1, str2);
    }

//    @PostMapping("sameKotlin")
//    @ResponseBody
//    public float sameKotlin(@RequestBody Map<String, String> param) {
//        String str1 = param.get("str1");
//        String str2 = param.get("str2");
//        return TestUtilKt.getSimilarityRatio(str1, str2);
//    }
}
