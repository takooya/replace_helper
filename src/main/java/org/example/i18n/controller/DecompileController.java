package org.example.i18n.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.i18n.domain.bases.ReplaceInfo;
import org.example.i18n.domain.dto.Multi2OnePatternPart;
import org.example.i18n.domain.param.AddMethodCommentParam;
import org.example.i18n.domain.param.BlockAppendParam;
import org.example.i18n.domain.param.LoopFile2MapParam;
import org.example.i18n.domain.param.Multi2OneParam;
import org.example.i18n.domain.param.OnlySourceParam;
import org.example.i18n.domain.param.ThisAppendParam;
import org.example.i18n.exceptions.JumpOutException;
import org.example.i18n.temp.MainUtil;
import org.example.i18n.utils.DirectoryUtil;
import org.example.i18n.utils.ProcessExecutor;
import org.example.i18n.utils.rowformat.BlockCommentInfo;
import org.example.i18n.utils.rowformat.CodeTypeEnum;
import org.example.i18n.utils.rowformat.DealRowInfo;
import org.example.i18n.utils.rowformat.LineUtil;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
            Map<Integer, DealRowInfo> infoMap = param.getCodeType().deal(info);
            // 修改后的目标文件行数据
            List<String> afterUpdateAll = new ArrayList<>();
            for (Integer rowNum : infoMap.keySet()) {
                String row = infoMap.get(rowNum).getOrigin();
                String value = infoMap.get(rowNum).getDealed();
                String trimValue = value.trim();
                String result;
                if (StrUtil.isBlank(trimValue)) {
                    afterUpdateAll.add(row);
                    continue;
                }
                DealRowInfo rowComment = LineUtil.getRowComment(row);
                String comment = rowComment.getDealed();
                // 2# regExpMap
                // 判断key(正则表达式)是否匹配,如果匹配,按value替换
                Map<String, String> regExpMap = allInfo.getRegExpMap();
                Optional<String> any = regExpMap.keySet().stream().filter(s -> {
                    Pattern compile = Pattern.compile(s);
                    Matcher matcher = compile.matcher(trimValue);
                    return param.getEqualType().match(matcher);
                }).findAny();
                if (any.isPresent()) {
                    String matchExp = any.get();
                    String replaceExp = regExpMap.get(matchExp);
                    // 被正则表达式匹配到,按正则匹配修改
                    result = ReUtil.replaceAll(trimValue, matchExp, replaceExp);
                    result = result + comment;
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
            // 文件行
            List<String> lines = FileUtil.readLines(file, StandardCharsets.UTF_8);
            // 解析后的文件行信息
            Map<Integer, DealRowInfo> allInfo = param.getCodeType().deal(lines);
            // 正则匹配表达式列表
            Map<String, String> sourceRegExpMap = param.getSourceRegExp();
            // 正则替换表达式
            String targetRegExp = param.getTargetRegExp();
            // 一组正则匹配对象信息
            Multi2OnePatternPart matchTempRecord = new Multi2OnePatternPart();
            // 新数据行
            List<String> newFileLines = new ArrayList<>();
            boolean matched = false;
            // 第一次遍历,获取匹配数据
            for (Integer rowNum : allInfo.keySet()) {
                DealRowInfo rowInfo = allInfo.get(rowNum);
                String origin = rowInfo.getOrigin();
                String dealRow = rowInfo.getDealed();

                String firstReg = sourceRegExpMap.get("1");
                Pattern firstPattern = Pattern.compile(firstReg);
                Matcher firstMatcher = firstPattern.matcher(dealRow);
                if (matchTempRecord.isEmpty()) {
                    // 上一行没有匹配过
                    if (!firstMatcher.matches()) {
                        // 第一行没有匹配
                        newFileLines.add(origin);
                    } else {
                        // 第一行匹配
                        matchTempRecord.addRecordLine(1, new DealRowInfo(origin, dealRow));
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
                        Matcher regMatcher = regPattern.matcher(origin.trim());
                        matchline = param.getEqualType().match(regMatcher);
                    }
                    if (matchline) {
                        // 上一行匹配,当前行再次匹配
                        matchTempRecord.addRecordLine(regLineindex, new DealRowInfo(origin, dealRow));
                    } else {
                        // 上一行匹配,当前行不匹配
                        Collection<DealRowInfo> matchLines = matchTempRecord.values();
                        Collection<String> regExps = sourceRegExpMap.values();
                        if (matchLines.size() != regExps.size()) {
                            log.info("[-DecompileController:multiLine2single-]部分匹配,数据舍弃:{}", matchLines);
                            List<String> matchOrigins = matchLines.stream().map(DealRowInfo::getOrigin)
                                    .collect(Collectors.toList());
                            List<String> matchDealeds = matchLines.stream().map(DealRowInfo::getDealed)
                                    .collect(Collectors.toList());
                            newFileLines.addAll(matchOrigins);
                            newFileLines.add(origin);
                            matchTempRecord.clean();
                            param.addReplaceInfo(file.getAbsolutePath(), matchDealeds, null, "部分匹配,舍弃匹配数据!");
                            continue;
                        }
                        // 所有行trim后拼接
                        List<String> dealedLineList = matchLines
                                .stream()
                                .map((DealRowInfo t) -> t.getDealed().trim())
                                .collect(Collectors.toList());
                        String dealedOneLine = String.join("", dealedLineList);
                        // 所有匹配正则拼接
                        String allRegJoin = String.join("", regExps);
                        // 新的行数据
                        String newLine = ReUtil.replaceAll(dealedOneLine, allRegJoin, targetRegExp);
                        // 添加新行
                        newFileLines.add(newLine);
                        // 添加当前行
                        newFileLines.add(origin);
                        // 清空匹配对象数据
                        matchTempRecord.clean();
                        param.addReplaceInfo(file.getAbsolutePath(), dealedLineList, newLine);
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
            String appendStr = param.getAppendRegStr();
            List<String> rowList = FileUtil.readLines(sourceFile, StandardCharsets.UTF_8);
            Map<Integer, DealRowInfo> sourceRowMap = param.getCodeType().deal(rowList);
            Map<String, DealRowInfo> dealedShortFocusMap = new HashMap<>(rowList.size());
            for (Integer rowNum : sourceRowMap.keySet()) {
                // 不包含关键字段的跳过逻辑处理
                String dealedRow = sourceRowMap.get(rowNum).getDealed();
                if (!ReUtil.contains(appendStr, dealedRow)) {
                    continue;
                }
                String dealedShortRow = dealedRow.replaceAll(appendStr, "").trim();
                if (!dealedShortFocusMap.containsKey(dealedShortRow)) {
                    dealedShortFocusMap.put(dealedShortRow, sourceRowMap.get(rowNum));
                } else {
                    if (!dealedShortFocusMap.get(dealedShortRow).equals(sourceRowMap.get(rowNum))) {
                        param.addReplaceInfo(sourceFile.getAbsolutePath(), "同一文件内,不同行在进行关键字排除后相同,但是原语句不同!");
                        return;
                    }
                }
            }
            Set<String> dealedShortSources = dealedShortFocusMap.keySet();
            String sourcePath = sourceFile.getAbsolutePath();
            String targetPath = sourcePath.replace(param.getSourcesPath(), param.getTargetsPath());
            File targetFile = new File(targetPath);
            if (!targetFile.exists()) {
                log.warn("[-DecompileController:accept-]目标文件不存在:{}", targetPath);
                return;
            }
            // 读取文件
            List<String> targetRows = FileUtil.readLines(targetFile, StandardCharsets.UTF_8);
            Map<Integer, DealRowInfo> targetRowMap = param.getCodeType().deal(targetRows);
            AtomicBoolean changeFlag = new AtomicBoolean(false);
            // 文件变更后的文件行
            List<String> resultRows = targetRowMap.keySet().stream().map(rowNum -> {
                // 目标文件行信息
                DealRowInfo targetRow = targetRowMap.get(rowNum);
                // 目标文件处理后的行信息
                String dealedTargetRow = targetRow.getDealed().trim();
                String result;
                if (StrUtil.isBlank(dealedTargetRow)) {
                    // 如果是空行,不处理
                    result = targetRow.getOrigin();
                } else if (dealedTargetRow.contains("//")) {
                    DealRowInfo sourceRowInfo = dealedShortFocusMap.get(dealedTargetRow);
                    if (sourceRowInfo == null) {
                        // 资源文件当前行匹配项为空
                        result = targetRow.getOrigin();
                    } else if (Objects.equals(targetRow.onlyComment().trim(), sourceRowInfo.onlyComment().trim())) {
                        // 资源文件与匹配文件注释相同
                        // 匹配到和资源文件一致的语句,直接返回资源文件的语句
                        result = dealedShortFocusMap.get(dealedTargetRow).getOrigin();
                    } else {
                        // 资源文件与匹配文件注释不相同
                        result = targetRow.getOrigin();
                        param.addReplaceInfo(
                                targetPath,
                                Collections.singletonList(targetRow.getOrigin()),
                                sourceRowInfo.getOrigin(),
                                "当前行含有注释,暂不处理");
                    }
                } else if (!dealedShortSources.contains(dealedTargetRow)) {
                    // 没有匹配到和资源文件一致的语句
                    result = targetRow.getOrigin();
                } else {
                    // 匹配到和资源文件一致的语句,直接返回资源文件的语句
                    result = dealedShortFocusMap.get(dealedTargetRow).getOrigin();
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
            Map<Integer, DealRowInfo> dealMap = CodeTypeEnum.REMOVE_BLOCK.deal(rowList);
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
                for (DealRowInfo dealRowInfo : dealMap.values()) {
                    if (Objects.equals(targetRow.trim(), dealRowInfo.onlyCode())) {
                        comment = dealRowInfo.onlyComment();
                        break;
                    }
                }
                String result = targetRow;
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

    /**
     * 去除目标文件夹下全部文件内的空行
     *
     * @param param check 检查还是修改
     *              sourcesPath 资源路径地址
     *              targetsType 目标类型
     *              filtersType 过滤类型
     * @return 检查结果或替换是否成功
     */
    @RequestMapping("/removeEmptyLine")
    public List<ReplaceInfo> removeEmptyLine(@Valid @RequestBody OnlySourceParam param) {
        // 获取资源路径
        String sourcesPath = param.getSourcesPath();
        // 获取资源文件夹
        File sources = MainUtil.checkAndGetFile(sourcesPath);
        // 变更计数
        AtomicInteger changeCount = new AtomicInteger();
        DirectoryUtil.loopFiles(sources, sourceFile -> {
            boolean pass = param.pass(sourceFile);
            if (pass) {
                return;
            }
            List<String> rowList = FileUtil.readLines(sourceFile, StandardCharsets.UTF_8);
            Map<Integer, DealRowInfo> dealMap = param.getCodeType().deal(rowList);
            List<String> withoutEmptyLines = dealMap.values().stream()
                    .filter(dealInfo -> StrUtil.isNotBlank(dealInfo.getDealed()))
                    .map(DealRowInfo::getOrigin)
                    .collect(Collectors.toList());
            int emptyLineCount = rowList.size() - withoutEmptyLines.size();
            if (emptyLineCount != 0) {
                changeCount.incrementAndGet();
                param.addReplaceInfo(sourceFile.getAbsolutePath(), "共计空行" + emptyLineCount);
            }
            if (!param.isCheck()) {
                FileUtil.writeLines(withoutEmptyLines, sourceFile, StandardCharsets.UTF_8);
            }
        });
        param.addReplaceInfo(sourcesPath, "总结: 文件变更计数,共计:" + changeCount.get());
        return param.getReplaceInfos();
    }

    /**
     * catch语句中,参数名不一致处理
     *
     * @param param check 检查还是修改
     *              sourcesPath 资源路径地址
     *              targetsType 目标类型
     *              filtersType 过滤类型
     * @return 检查结果或替换是否成功
     */
    @RequestMapping("/catchVariable")
    public List<ReplaceInfo> catchVariable(@Valid @RequestBody BlockAppendParam param) {
        // 获取资源路径
        String sourcesPath = param.getSourcesPath();
        // 获取资源文件夹
        File sources = MainUtil.checkAndGetFile(sourcesPath);
        // 变更计数
        AtomicInteger changeCount = new AtomicInteger();
        DirectoryUtil.loopFiles(sources, sourceFile -> {
            boolean pass = param.pass(sourceFile);
            if (pass) {
                return;
            }
            List<String> sourceRowList = FileUtil.readLines(sourceFile, StandardCharsets.UTF_8);
            Map<Integer, DealRowInfo> sourceDealMap = param.getCodeType().deal(sourceRowList);

            // 记录资源 开始
            Map<String, List<DealRowInfo>> sourceBlocks = new HashMap<>();
            List<DealRowInfo> sourceBlock = new ArrayList<>();
            int blockIncome = -1;
            // 记录资源
            for (Integer rowNum : sourceDealMap.keySet()) {
                // 当计数器清零,则计数器复位
                if (blockIncome == 0) {
                    blockIncome = -1;
                    String allRow = sourceBlock.stream()
                            .map(dealRowInfo -> dealRowInfo.getOrigin().trim())
                            .collect(Collectors.joining());
                    sourceBlocks.put(allRow, new ArrayList<>(sourceBlock));
                    sourceBlock = new ArrayList<>();
                }
                DealRowInfo dealRowInfo = sourceDealMap.get(rowNum);
                String keyword = param.getAppendRegStr();
                // 是否在块逻辑内,if: 不在块逻辑内; else: 在块逻辑内
                if (blockIncome == -1) {
                    // 判断当前行是否包含关键字或正则
                    if (ReUtil.contains(keyword, dealRowInfo.getDealed())) {
                        // 计数器未初始化时,发现定位字符串,则开始计数,数量为开始标签出现次数
                        blockIncome = StrUtil.count(dealRowInfo.getDealed(), param.getBlockStartMark());
                        sourceBlock.add(dealRowInfo);
                    }
                } else {
                    blockIncome -= StrUtil.count(dealRowInfo.getDealed(), param.getBlockEndMark());
                    sourceBlock.add(dealRowInfo);
                }
            }
            // 记录资源 结束

            String sourcePath = sourceFile.getAbsolutePath();
            String targetPath = sourcePath.replace(sourcesPath, param.getTargetsPath());
            File targetFile = new File(targetPath);
            if (!targetFile.exists()) {
                log.info("[-DecompileController:catchVariable-]不存在对应的java文件:{}", sourcePath);
                return;
            }

            List<String> targetRowList = FileUtil.readLines(targetFile, StandardCharsets.UTF_8);
            Map<Integer, DealRowInfo> targetDealMap = param.getCodeType().deal(targetRowList);
            Map<Integer, DealRowInfo> targetTempBlock = new HashMap<>();
            Map<Integer, DealRowInfo> resultDealMap = new HashMap<>();
            for (Integer rowNum : targetDealMap.keySet()) {
                if (blockIncome == 0) {
                    blockIncome = -1;
                    // todo 这里做匹配逻辑 关键逻辑
                    String targetBlockKey = targetTempBlock.values().stream()
                            .map(dealRowInfo -> dealRowInfo.getOrigin().trim())
                            .collect(Collectors.joining());
                    for (String sourceBlockKey : sourceBlocks.keySet()) {
                        String sourceKeyMatchReg = ReUtil.get(param.getKeyMatchReg(), sourceBlockKey, 1);
                        String targetKeyMatchReg = ReUtil.get(param.getKeyMatchReg(), targetBlockKey, 1);
                        String dealedTargetBlockKey = ReUtil.replaceAll(targetBlockKey, "([^a-zA-Z^])" + targetKeyMatchReg + "([^a-zA-Z^])", "$1" + sourceKeyMatchReg + "$2");
                        if (!Objects.equals(sourceKeyMatchReg, targetKeyMatchReg) &&
                                StrUtil.similar(sourceBlockKey, dealedTargetBlockKey) > param.getSimilar()) {
                            // 语句相似度大于80%
                            Map<Integer, DealRowInfo> finalTargetTempBlock = targetTempBlock;
                            Map<Integer, DealRowInfo> importants = targetTempBlock.keySet().stream()
                                    .collect(Collectors.toMap(
                                            tempRowNum -> tempRowNum,
                                            tempRowNum -> {
                                                DealRowInfo inBlockRow = finalTargetTempBlock.get(tempRowNum);
                                                String important = ReUtil.replaceAll(inBlockRow.getOrigin(),
                                                        "([^a-zA-Z^])" + targetKeyMatchReg + "([^a-zA-Z^])",
                                                        "$1" + sourceKeyMatchReg + "$2");
                                                inBlockRow.setOrigin(important);
                                                return inBlockRow;
                                            }
                                    ));
                            resultDealMap.putAll(importants);
                            param.addReplaceInfo(targetPath,
                                    targetTempBlock.values().stream()
                                            .map(DealRowInfo::getOrigin)
                                            .collect(Collectors.toList()),
                                    importants.values().stream()
                                            .map(DealRowInfo::getOrigin)
                                            .collect(Collectors.joining("\n")));
                            changeCount.incrementAndGet();
                        } else {
                            resultDealMap.putAll(targetTempBlock);
                        }
                    }
                    targetTempBlock = new HashMap<>();
                }
                DealRowInfo targetRowInfo = targetDealMap.get(rowNum);
                String keyword = param.getAppendRegStr();
                // 是否在块逻辑内,if: 不在块逻辑内; else: 在块逻辑内
                if (blockIncome == -1) {
                    // 判断当前行是否包含关键字或正则
                    if (ReUtil.contains(keyword, targetRowInfo.getDealed())) {
                        // 计数器未初始化时,发现定位字符串,则开始计数,数量为开始标签出现次数
                        blockIncome = StrUtil.count(targetRowInfo.getDealed(), param.getBlockStartMark());
                        targetTempBlock.put(rowNum, targetRowInfo);
                    } else {
                        resultDealMap.put(rowNum, targetRowInfo);
                    }
                } else {
                    blockIncome -= StrUtil.count(targetRowInfo.getDealed(), param.getBlockEndMark());
                    targetTempBlock.put(rowNum, targetRowInfo);
                }
            }
            if (!param.isCheck()) {
                List<String> resultRowList = resultDealMap.values().stream()
                        .map(DealRowInfo::getOrigin)
                        .collect(Collectors.toList());
                FileUtil.writeLines(resultRowList, targetFile, StandardCharsets.UTF_8);
            }
        });
        param.addReplaceInfo(sourcesPath, "总结: 文件变更计数,共计:" + changeCount.get());
        if (param.isCheck()) {
            return param.getReplaceInfos();
        } else {
            throw new JumpOutException("文件处理完成,共计处理" + changeCount.get() + "处代码!", 200);
        }
    }


    /**
     * 添加方法注释
     *
     * @param param check 检查还是修改
     *              sourcesPath 资源路径地址
     *              targetsPath 目标路径地址
     *              targetsType 目标类型
     *              filtersType 过滤类型
     * @return 检查结果或替换是否成功
     */
    @RequestMapping("/addMethodComment")
    public List<JSONObject> addMethodComment(@Valid @RequestBody AddMethodCommentParam param) {
        // 获取资源路径
        String sourcesPath = param.getSourcesPath();
        // 获取资源文件夹
        File sources = MainUtil.checkAndGetFile(sourcesPath);
        param.setSources(sources);

        // 变更计数
        AtomicInteger changeCount = new AtomicInteger();
        DirectoryUtil.loopFiles(sources, sourceFile -> {
            if (param.getResults().size() > param.getLimit()) {
                return;
            }
            boolean pass = param.pass(sourceFile);
            if (pass) {
                return;
            }
            List<String> rowList = FileUtil.readLines(sourceFile, StandardCharsets.UTF_8);
            Map<Integer, DealRowInfo> sourceDealMap = CodeTypeEnum.REMOVE_BLOCK.deal(rowList);
            Map<String, Map<Integer, DealRowInfo>> sourceResult = new HashMap<>();
            BlockCommentInfo blockCommentInfo = new BlockCommentInfo();
            for (Integer rowNum : sourceDealMap.keySet()) {
                DealRowInfo sourceRowInfo = sourceDealMap.get(rowNum);
                String source = sourceRowInfo.getOrigin();
                Pattern pattern = Pattern.compile(param.getRegExp());
                Matcher matcher = pattern.matcher(source);
                if (!param.getEqualType().match(matcher)) {
                    continue;
                }
                Map<Integer, DealRowInfo> reverseRowInfoMap = new HashMap<>();
                boolean inComments = false;

                DealRowInfo lastRowInfo = sourceDealMap.get(rowNum - 1);
                if (ReUtil.contains(param.getBlockEndMark(), lastRowInfo.getOrigin())) {
                    inComments = true;
                }
                int tempReverseIndex = rowNum;
                while (inComments) {
                    DealRowInfo curReverseRow = sourceDealMap.get(--tempReverseIndex);
                    if (ReUtil.contains(param.getBlockStartMark(), lastRowInfo.getOrigin())) {
                        inComments = false;
                    }
                    reverseRowInfoMap.put(tempReverseIndex, curReverseRow);
                }
                String methodRow = sourceRowInfo.getDealed();
                sourceResult.put(methodRow, reverseRowInfoMap);
            }
            if (CollUtil.isNotEmpty(sourceResult)) {
                param.addResult(new JSONObject(sourceResult));
            }
        });
        return param.getResults();
    }
}
