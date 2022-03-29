package org.example.i18n.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.i18n.domain.param.LoopFile2MapParam;
import org.example.i18n.domain.param.Multi2OneParam;
import org.example.i18n.domain.dto.Multi2OnePatternDto;
import org.example.i18n.domain.bases.ReplaceInfo;
import org.example.i18n.domain.dto.ThisAppendParam;
import org.example.i18n.exceptions.JumpOutException;
import org.example.i18n.temp.MainUtil;
import org.example.i18n.utils.DirectoryUtil;
import org.example.i18n.utils.ProcessExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@Slf4j
@RestController
@RequestMapping("/decompile")
public class DecompileController {
    static String jadPath = "C:\\Users\\Administrator\\IdeaProjects\\I18n\\src\\main\\resources\\jad.exe";

    /**
     * 此方法不可使用 输入输出流相互阻塞
     *
     * @param param Map
     *              source: 目标目录
     */
    @Deprecated
    public void base(@RequestBody Map<String, String> param) {
        String sourcePath = param.get("source");
        File source = new File(sourcePath);
        List<String> results = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        DirectoryUtil.loopFiles(source, (Consumer<File>) file -> {
            String classPath = file.getAbsolutePath();
            String commend;
            if (!classPath.endsWith(".class")) {
                return;
            }
            String javaPath = classPath.replace(".class", ".java");
            commend = jadPath + " -s java -o -p " + classPath;
            Process exec;
            try {
                exec = Runtime.getRuntime().exec(commend);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            String execResult = IoUtil.read(exec.getInputStream(), StandardCharsets.UTF_8);
            String errResult = IoUtil.read(exec.getErrorStream(), StandardCharsets.UTF_8);
            if (StrUtil.isNotBlank(execResult)) {
                // 同级文件夹写回java文件
                FileUtil.writeBytes(execResult.getBytes(StandardCharsets.UTF_8), javaPath);
                // 记录成功记录
                results.add("[success]" + javaPath);
            } else {
                // 记录错误日志
                errors.add("[ERROR : " + classPath + "]" + errResult);
            }
        });
    }

    /**
     * 反编译指定文件夹下的class文件
     *
     * @param param Map
     *              source:目标路径
     * @return Map
     * * results: 反编译的java文件路径
     * * errors: 反编译失败的class文件路径
     */
    @PostMapping("/exec")
    public Map<String, List<String>> exec(@RequestBody Map<String, String> param) {
        String sourcePath = param.get("source");
        File source = new File(sourcePath);
        List<String> results = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        DirectoryUtil.loopFiles(source, (Consumer<File>) file -> {
            String classPath = file.getAbsolutePath();
            String commend;
            if (!classPath.endsWith(".class")) {
                return;
            }
            String javaPath = classPath.replace(".class", ".java");
            commend = jadPath + " -s java -o -p " + classPath;
            ProcessExecutor instance;
            String execResult = null;
            String errResult;
            try {
                instance = ProcessExecutor.getInstance(commend);
                execResult = instance.getSuccess();
                errResult = instance.getError();
            } catch (IOException e) {
                e.printStackTrace();
                errResult = "[ERROR:IOException]: " + e.getMessage();
            }
            if (CharSequenceUtil.isNotBlank(execResult)) {
                // 同级文件夹写回java文件
                assert execResult != null;
                FileUtil.writeBytes(execResult.getBytes(StandardCharsets.UTF_8), javaPath);
                // 记录成功记录
                results.add("[success]" + javaPath);
                FileUtil.del(classPath);
            } else {
                // 记录错误日志
                errors.add("[ERROR : " + classPath + "]" + errResult);
            }
            log.info("[-DecompileController:test4-]classPath:{}", classPath);
        });
        return MapUtil.builder("results", results)
                .put("errors", errors).build();
    }

    /**
     * 按正则匹配,修改路径下所有文件
     *
     * @param param 替换入参: (目标地址; 过滤正则; 类型过滤条件; 是否是检查等)
     * @return 检查结果或替换是否成功
     */
    @RequestMapping("/old2new")
    public List<ReplaceInfo> old2new(@Valid @RequestBody LoopFile2MapParam param) {
        // 获取资源路径
        String sourcesPath = param.getSourcesPath();
        // 获取资源文件夹
        File sources = MainUtil.checkAndGetFile(sourcesPath);
        param.setSources(sources);

        DirectoryUtil.loopFiles2Map(param, allInfo -> {
            // 上述资源文件夹下的某一个资源文件
            File source = allInfo.getSources();
            // 判断是否有类型过滤
            boolean pass = param.pass(source);
            if (pass) {
                return;
            }
            // 读取出目标文件行数据
            List<String> info = FileUtil.readLines(source, StandardCharsets.UTF_8);
            // 修改后的目标文件行数据
            List<String> afterUpdateAll = new ArrayList<>();
            boolean longMark = false;
            for (String row : info) {
                String result;
                String trimRow = row.trim();
                if (StrUtil.isBlankOrUndefined(trimRow)) {
                    afterUpdateAll.add(row);
                    continue;
                }
                // 如果以/*开始,但并未以*/结束的代码段
                if (longMark || trimRow.startsWith("/*")) {
                    longMark = !(trimRow.contains("*/"));
                    afterUpdateAll.add(row);
                    continue;
                }
                // 如果以//开始
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
                    // 被正则表达式匹配到,按正则匹配修改
                    result = ReUtil.replaceAll(trimRow, any.get(), regExpMap.get(any.get()));
                    result = comment == null ? result : result + comment;
                    // 记录当前文件名,修改前行数据,修改后行数据
                    param.addReplaceInfo(new ReplaceInfo(allInfo.getSourcesPath(), row, result));
                } else {
                    // 正则表达式不匹配,保持原结构
                    result = row;
                }
                afterUpdateAll.add(result);
            }
            if (!allInfo.isCheck()) {
                // 覆盖模式
                FileUtil.writeLines(afterUpdateAll, source, StandardCharsets.UTF_8);
            }
        });
        return param.getReplaceInfos();
    }

    /**
     * 现在部分文件夹里含有重复的文件
     * * 同名class和java文件,有java文件的需要删除class文件,反之不删除
     *
     * @param param 通用入参: (目标地址; 过滤正则; 类型过滤条件; 是否是检查等)
     * @return 检查结果或删除过程信息
     */
    @RequestMapping("/deleteFiles")
    public List<ReplaceInfo> deleteFiles(@Valid @RequestBody LoopFile2MapParam param) {
        // 获取资源路径
        String sourcesPath = param.getSourcesPath();
        // 获取资源文件夹
        File sources = MainUtil.checkAndGetFile(sourcesPath);
        // 删除文件计数器
        AtomicInteger delFileCount = new AtomicInteger();
        DirectoryUtil.loopFiles(sources, classFile -> {
            String classPath = classFile.getAbsolutePath();
            if (!classPath.endsWith(".class")) {
                return;
            }
            String javaPath = classPath.replace(".class", ".java");
            File javaFile = new File(javaPath);
            if (javaFile.exists()) {
                if (param.isCheck()) {
                    // 检查逻辑
                    param.addReplaceInfo(classPath, "有对应java文件!");
                    delFileCount.incrementAndGet();
                } else {
                    // 持久化逻辑
                    boolean delete = classFile.delete();
                    if (!delete) {
                        param.addReplaceInfo(classPath, "文件删除失败!");
                    } else {
                        delFileCount.incrementAndGet();
                    }
                }
            } else {
                param.addReplaceInfo(classPath, "不存在反编译的java文件!");
            }
        });
        param.addReplaceInfo(sourcesPath, "总信息: class文件共计" + delFileCount.get() + "个");
        return param.getReplaceInfos();
    }

    String repeatMark = "( \\(\\d\\))";
    Pattern repeatMarkLP = Pattern.compile(repeatMark);

    /**
     * 现在部分文件夹里含有重复的文件,
     * * 有同名的非class和java文件,有的文件名中带(1)/(2)/(3)的重复标识符,需要对比
     *
     * @param param 通用入参: (目标地址; 过滤正则; 类型过滤条件; 是否是检查等)
     * @return 检查结果或删除过程信息
     */
    @RequestMapping("/delRepeatFile")
    public List<ReplaceInfo> delRepeatFile(@Valid @RequestBody LoopFile2MapParam param) {
        // 获取资源路径
        String sourcesPath = param.getSourcesPath();
        // 获取资源文件夹
        File sources = MainUtil.checkAndGetFile(sourcesPath);
        // 删除文件计数器
        AtomicInteger delFileCount = new AtomicInteger();
        DirectoryUtil.loopFiles(sources, file -> {
            String sourcePath = file.getAbsolutePath();
            Matcher matcher = repeatMarkLP.matcher(sourcePath);
            if (!matcher.find()) {
                return;
            }
            String firstG = matcher.group(1);
            String targetPath = sourcePath.replace(firstG, "");
            if (!FileUtil.exist(targetPath)) {
                return;
            }
            String repeatContent = FileUtil.readString(sourcePath, StandardCharsets.UTF_8);
            String originContent = FileUtil.readString(targetPath, StandardCharsets.UTF_8);
            if (!repeatContent.equals(originContent)) {
                param.addReplaceInfo(sourcePath, "文件内容与原文件不一致");
            }
            if (param.isCheck()) {
                param.addReplaceInfo(sourcePath, "需删除的文件");
                delFileCount.incrementAndGet();
            } else {
                boolean del = FileUtil.del(sourcePath);
                if (del) {
                    delFileCount.incrementAndGet();
                }
            }
        });
        param.addReplaceInfo(sourcesPath, "总信息: 重复文件" + delFileCount.get() + "个");
        return param.getReplaceInfos();
    }

    /**
     * 多行数据转一行
     *
     * @param param check 检查还是修改
     *              sourcePath 资源路径地址
     *              sourceRegExp 匹配正则
     *              targetRegExp 替换正则
     * @return 检查结果或替换是否成功
     */
    @RequestMapping("/multiLine2single")
    public List<ReplaceInfo> multiLine2single(@Valid @RequestBody Multi2OneParam param) {
        // 获取资源路径
        String sourcePath = param.getSourcesPath();
        // 获取资源文件夹
        File source = MainUtil.checkAndGetFile(sourcePath);
        // 删除文件计数器
        AtomicInteger rowCount = new AtomicInteger();
        DirectoryUtil.loopFiles(source, file -> {
            List<String> lines = FileUtil.readLines(file, StandardCharsets.UTF_8);
            Map<String, String> sourceRegExpMap = param.getSourceRegExp();
            String targetRegExp = param.getTargetRegExp();
            Multi2OnePatternDto matchTempRecord = new Multi2OnePatternDto();
            List<String> newFileLines = new ArrayList<>();
            boolean matched = false;
            // 第一次遍历,获取匹配数据
            for (String line : lines) {
                String firstReg = sourceRegExpMap.get("1");
                Pattern firstPattern = Pattern.compile(firstReg);
                Matcher firstMatcher = firstPattern.matcher(line.trim());
                if (matchTempRecord.isEmpty()) {
                    // 上一行没有匹配过
                    if (!firstMatcher.matches()) {
                        // 第一行没有匹配
                        newFileLines.add(line);
                    } else {
                        // 第一行匹配
                        matchTempRecord.addRecordLine(1, line);
                        // 本文件是否被匹配过
                        matched = true;
                    }
                } else {
                    // 上一行有被匹配过
                    int regLineindex = matchTempRecord.size() + 1;
                    boolean matchline = false;
                    if (regLineindex <= sourceRegExpMap.size()) {
                        String regString = sourceRegExpMap.get(String.valueOf(regLineindex));
                        Pattern regPattern = Pattern.compile(regString);
                        Matcher regMatcher = regPattern.matcher(line.trim());
                        matchline = regMatcher.matches();
                    }
                    if (matchline) {
                        // 上一行匹配,当前行再次匹配
                        matchTempRecord.addRecordLine(regLineindex, line);
                    } else {
                        // 上一行匹配,当前行不匹配
                        Collection<String> matchLines = matchTempRecord.values();
                        Collection<String> regExps = sourceRegExpMap.values();
                        if (matchLines.size() != regExps.size()) {
                            log.info("[-DecompileController:multiLine2single-]部分匹配,数据舍弃:{}", matchLines);
                            newFileLines.addAll(matchLines);
                            newFileLines.add(line);
                            matchTempRecord.clean();
                            param.addReplaceInfo(file.getAbsolutePath(), new ArrayList<>(matchLines), null, "部分匹配,舍弃匹配数据!");
                            continue;
                        }
                        // 所有行trim后拼接
                        String allLines = matchLines
                                .stream()
                                .map(String::trim)
                                .collect(Collectors.joining());
                        // 所有匹配正则拼接
                        String allRegJoin = String.join("", regExps);
                        // 新的行数据
                        String newLine = ReUtil.replaceAll(allLines, allRegJoin, targetRegExp);
                        // 添加新行
                        newFileLines.add(newLine);
                        // 添加当前行
                        newFileLines.add(line);
                        // 清空匹配对象数据
                        matchTempRecord.clean();
                        param.addReplaceInfo(file.getAbsolutePath(), new ArrayList<>(matchLines), newLine);
                    }
                }
            }
            if (matched) {
                rowCount.incrementAndGet();
                if (!param.isCheck()) {
                    FileUtil.writeLines(newFileLines, file, StandardCharsets.UTF_8, false);
                }
            }
        });
        if (param.isCheck()) {
            return param.getReplaceInfos();
        } else {
            throw new JumpOutException("multiLine to single files: " + rowCount.get(), 200);
        }
    }

    /**
     * 根据资源文件和目标文件,将特定字符串拼装到目标文件上
     * 资源文件包含待拼接字符串
     * 目标文件不包含拼接字符串
     *
     * @param param check 检查还是修改
     *              sourcesPath 资源路径地址
     *              targetsPath 目标路径地址
     *              appendStr 拼接字符串
     *              targetsType 目标类型
     *              filtersType 过滤类型
     * @return 检查结果或替换是否成功
     */
    @RequestMapping("/appendStr")
    public List<ReplaceInfo> appendStr(@Valid @RequestBody ThisAppendParam param) {
        // 获取资源路径
        String sourcesPath = param.getSourcesPath();
        // 获取资源文件夹
        File sources = MainUtil.checkAndGetFile(sourcesPath);
        param.setSources(sources);
        // 变更计数
        AtomicInteger changeCount = new AtomicInteger();
        DirectoryUtil.loopFiles(sources, sourceFile -> {
            boolean pass = param.pass(sourceFile);
            if (pass) {
                return;
            }
            String appendStr = param.getAppendStr();
            List<String> rowList = FileUtil.readLines(sourceFile, StandardCharsets.UTF_8);
            Map<String, String> locationSources = new HashMap<>();
            for (String row : rowList) {
                if (!row.contains(appendStr)) {
                    continue;
                }
                String removeStrRow = row.replaceAll(appendStr, "").trim();
                String remove = locationSources.get(removeStrRow);
                if (StrUtil.isEmpty(remove)) {
                    locationSources.put(removeStrRow, row);
                }
            }
            Set<String> locationKeys = locationSources.keySet();
            String sourcePath = sourceFile.getAbsolutePath();
            String targetPath = sourcePath.replace(param.getSourcesPath(), param.getTargetsPath());
            File targetFile = new File(targetPath);
            if (!targetFile.exists()) {
                log.warn("[-DecompileController:accept-]目标文件不存在:{}", targetPath);
                return;
            }
            // 读取文件
            List<String> targetRows = FileUtil.readLines(targetFile, StandardCharsets.UTF_8);
            AtomicBoolean changeFlag = new AtomicBoolean(false);
            // 文件变更后的文件行
            List<String> resultRows = targetRows.stream().map(targetRow -> {
                String targetLocation = null;
                String result = targetRow;
                for (String locationKey : locationKeys) {
                    if (targetRow.trim().contains(locationKey)) {
                        targetLocation = locationKey;
                        break;
                    }
                }
                if (StrUtil.isNotBlank(targetLocation)) {
                    boolean containsComment = targetRow.contains("//");
                    if (containsComment) {
                        int commentIndex = targetRow.indexOf("//");
                        String comment = targetRow.substring(commentIndex);
                        result = locationSources.get(targetLocation) + comment;
                    } else {
                        result = locationSources.get(targetLocation);
                    }
                    // 修改标示变更
                    changeFlag.set(true);
                    // 变更计数
                    changeCount.getAndIncrement();
                    // check结果拼装
                    param.addReplaceInfo(targetPath, result);
                }
                return result;
            }).collect(Collectors.toList());
            if (changeFlag.get()) {
                if (!param.isCheck()) {
                    FileUtil.writeLines(resultRows, targetFile, StandardCharsets.UTF_8, false);
                }
            }
        });
        if (param.isCheck()) {
            return param.getReplaceInfos();
        } else {
            throw new JumpOutException("文件变更计数,共计:" + changeCount.get(), 200);
        }
    }

    /**
     * 根据资源文件和目标文件,将行的尾行注释从资源文件添加到目标文件
     * * 资源文件包含尾行注释的原文件
     * * 目标文件不包含包含尾行注释的反编译java文件
     *
     * @param param check 检查还是修改
     *              sourcesPath 资源路径地址
     *              targetsPath 目标路径地址
     *              appendStr 拼接字符串
     *              targetsType 目标类型
     *              filtersType 过滤类型
     * @return 检查结果或替换是否成功
     */
    @RequestMapping("/appendLineComment")
    public List<ReplaceInfo> appendLineComment(@Valid @RequestBody ThisAppendParam param) {
        // 获取资源路径
        String sourcesPath = param.getSourcesPath();
        // 获取资源文件夹
        File sources = MainUtil.checkAndGetFile(sourcesPath);
        param.setSources(sources);

        // 变更计数
        AtomicInteger changeCount = new AtomicInteger();
        DirectoryUtil.loopFiles(sources, sourceFile -> {
            boolean pass = param.pass(sourceFile);
            if (pass) {
                return;
            }
            List<String> rowList = FileUtil.readLines(sourceFile, StandardCharsets.UTF_8);
            Map<String, String> trimWithoutCommentMap = new HashMap<>();
            for (String row : rowList) {
                if (!row.trim().contains("//")) {
                    continue;
                }
                if (row.trim().startsWith("//")) {
                    continue;
                }
                String code = row.trim().substring(0, row.trim().indexOf("//"));
                String comment = row.trim().substring(row.trim().indexOf("//"));
                trimWithoutCommentMap.put(code, comment);
            }
            String sourcePath = sourceFile.getAbsolutePath();
            String targetPath = sourcePath.replace(param.getSourcesPath(), param.getTargetsPath());
            File targetFile = new File(targetPath);
            if (!targetFile.exists()) {
                log.warn("[-DecompileController:accept-]目标文件不存在:{}", targetPath);
                param.addReplaceInfo(targetPath, "目标文件不存在");
                return;
            }
            // 读取文件
            List<String> targetRows = FileUtil.readLines(targetFile, StandardCharsets.UTF_8);
            // 文件变更标识
            AtomicBoolean fileChangeFlag = new AtomicBoolean(false);
            // 文件变更后的文件行
            List<String> resultRows = targetRows.stream().map(targetRow -> {
                String comment = null;
                String result = targetRow;
                for (String codeKey : trimWithoutCommentMap.keySet()) {
                    if (targetRow.trim().equals(codeKey)) {
                        comment = trimWithoutCommentMap.get(codeKey);
                        break;
                    }
                }
                if (StrUtil.isNotBlank(comment)) {
                    result = result + comment;
                    // 文件修改标示变更
                    fileChangeFlag.set(true);
                    // 文件夹变更计数
                    changeCount.getAndIncrement();
                    // check结果拼装
                    param.addReplaceInfo(targetPath, result);
                }
                return result;
            }).collect(Collectors.toList());
            if (fileChangeFlag.get()) {
                if (!param.isCheck()) {
                    FileUtil.writeLines(resultRows, targetFile, StandardCharsets.UTF_8, false);
                }
            }
        });
        if (param.isCheck()) {
            return param.getReplaceInfos();
        } else {
            throw new JumpOutException("文件变更计数,共计:" + changeCount.get(), 200);
        }
    }
}
