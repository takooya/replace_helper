package org.example.i18n.temp;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.CharSequenceUtil;
import org.example.i18n.domain.dto.LoopFileParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static org.example.i18n.consts.CommonConstant.chineseLP;
import static org.example.i18n.consts.CommonConstant.startChineseLP;

/**
 * MainController临时辅助类
 *
 * @author wangyichun
 * @since 2022/1/4 18:17
 */
public interface MainUtil {
    static Map<String, String> getChinakeyProp(MultipartFile prop) throws IOException {
        String read = IoUtil.read(prop.getInputStream(), StandardCharsets.UTF_8);
        Map<String, String> chinakeyProp = null;
        if (!CharSequenceUtil.isNullOrUndefined(read)) {
            String[] linesArray = read.split("\n");
            chinakeyProp = new HashSet<>(Arrays.asList(linesArray)).stream()
                    .collect(HashMap::new, (BiConsumer<Map<String, String>, String>) (map, s) -> {
                        if (s.trim().startsWith("#") || !s.contains("=")) {
                            return;
                        }
                        s = s.replace("\r", "");
                        int eqindex = s.indexOf("=");
                        String key = s.substring(eqindex + 1);
                        String value = s.substring(0, eqindex);
                        map.put(key, value);
                    }, Map::putAll);
        }
        if (CollUtil.isEmpty(chinakeyProp)) {
            throw new RuntimeException("上传的资源文件内容错误");
        }
        return chinakeyProp;
    }

    static File checkAndGetDir(String sourcePath) {
        if (CharSequenceUtil.isBlankOrUndefined(sourcePath)) {
            throw new RuntimeException("目标地址不正确");
        }
        File source = new File(sourcePath);
        if (!source.exists() || !source.isDirectory()) {
            throw new RuntimeException("目标地址不正确");
        }
        return source;
    }

    static File checkAndGetFile(String sourcePath) {
        if (CharSequenceUtil.isBlankOrUndefined(sourcePath)) {
            throw new RuntimeException("目标地址不正确");
        }
        File source = new File(sourcePath);
        if (!source.exists()) {
            throw new RuntimeException("目标地址不正确");
        }
        return source;
    }

    static void importAndAutowiredAppend(Set<String> fullPathAndName) {
        fullPathAndName.forEach(s -> {
            File file = new File(s);
            List<String> lines = cn.hutool.core.io.FileUtil.readLines(file, StandardCharsets.UTF_8);
            final boolean[] finish = {false, false};
            List<String> targetLines = lines.stream().map(s1 -> {
                if (!finish[0] && s1.startsWith("package ")) {
                    s1 = s1 +
                            "\n" +
                            "import com.qm.tds.util.I18nUtil;\n";
                    finish[0] = true;
                }
                if (!finish[1] && s1.startsWith("public class ")) {
                    s1 = s1 + "\n" +
                            "    @Autowired\n" +
                            "    private I18nUtil i18nUtil;\n";
                    finish[1] = true;
                }
                return s1;
            }).collect(Collectors.toList());
            FileUtil.writeLines(targetLines, file, StandardCharsets.UTF_8);
        });
    }

    static void generateProperties(LoopFileParam<Set<String>> param) {
        File source = param.getSources();
        File target = param.getTarget();
        Set<String> filterSet = param.getData();
        if ("SwaggerConfig.java".equals(source.getName())) {
            return;
        }
        List<String> info = FileUtil.readLines(source, StandardCharsets.UTF_8);
        boolean comment = false;
        for (String row : info) {
            if (row.contains("/*")) {
                comment = true;
            }
            if (row.contains("*/")) {
                comment = false;
            }
            Matcher matcher = chineseLP.matcher(row);
            if (matcher.find()) {
                String trimRow = row.trim();
                boolean ignore = trimRow.startsWith("*");
                if (ignore) {
                    continue;
                }
                ignore = trimRow.startsWith("//");
                if (ignore) {
                    continue;
                }
                ignore = trimRow.startsWith("@Api");
                if (ignore) {
                    continue;
                }
                ignore = trimRow.startsWith("@Excel");
                if (ignore) {
                    continue;
                }
                ignore = trimRow.startsWith("log.");
                if (ignore) {
                    continue;
                }
                ignore = trimRow.startsWith("System.out.print");
                if (ignore) {
                    continue;
                }
                int i = trimRow.indexOf("//");
                String filter;
                if (i != -1) {
                    filter = trimRow.substring(0, i);
                    matcher = chineseLP.matcher(filter);
                    if (!matcher.find()) {
                        continue;
                    }
                } else {
                    filter = trimRow;
                }
                int j = trimRow.indexOf("/*");
                if (j != -1) {
                    filter = filter.substring(0, j);
                    matcher = chineseLP.matcher(filter);
                    if (!matcher.find()) {
                        continue;
                    }
                }
                if (comment) {
                    continue;
                }
                matcher = startChineseLP.matcher(filter);
                if (matcher.find()) {
                    continue;
                }
                String[] split = filter.split("\"");
                List<String> chSentences = Arrays.stream(split).filter(s -> {
                    Matcher innerMatch = chineseLP.matcher(s);
                    return innerMatch.find();
                }).collect(Collectors.toList());
//          todo      DynamicDataSourceContextHolder.java=/** 这个类不能实例化 */
                if (chSentences.size() > 1) {
                    FileUtil.appendUtf8String(
                            source.getName() + "=" + trimRow + "\n",
                            target);
                } else {
                    boolean contains = filterSet.contains(chSentences.get(0));
                    if (!contains) {
                        filterSet.add(chSentences.get(0));
                        FileUtil.appendUtf8String(
                                source.getName() + "=" + chSentences.get(0) + "\n",
                                target);
                    }
                }
            }
        }
    }

    static List<String> recordList(List<String> source) {
        boolean comment = false;
        List<String> target = new ArrayList<>();
        for (String row : source) {
            if (row.contains("/*")) {
                comment = true;
            }
            if (row.contains("*/")) {
                comment = false;
            }
            Matcher matcher = chineseLP.matcher(row);
            if (matcher.find()) {
                String trimRow = row.trim();
                boolean ignore = trimRow.startsWith("*");
                if (ignore) {
                    continue;
                }
                ignore = trimRow.startsWith("//");
                if (ignore) {
                    continue;
                }
                ignore = trimRow.startsWith("@Api");
                if (ignore) {
                    continue;
                }
                ignore = trimRow.startsWith("@Excel");
                if (ignore) {
                    continue;
                }
                ignore = trimRow.startsWith("log.");
                if (ignore) {
                    continue;
                }
                ignore = trimRow.startsWith("System.out.print");
                if (ignore) {
                    continue;
                }
                int i = trimRow.indexOf("//");
                String filter;
                if (i != -1) {
                    filter = trimRow.substring(0, i);
                    matcher = chineseLP.matcher(filter);
                    if (!matcher.find()) {
                        continue;
                    }
                } else {
                    filter = trimRow;
                }
                int j = trimRow.indexOf("/*");
                if (j != -1) {
                    filter = filter.substring(0, j);
                    matcher = chineseLP.matcher(filter);
                    if (!matcher.find()) {
                        continue;
                    }
                }
                if (comment) {
                    continue;
                }
                matcher = startChineseLP.matcher(filter);
                if (matcher.find()) {
                    continue;
                }
                target.add(row);
            }
        }
        return target;
    }
}
