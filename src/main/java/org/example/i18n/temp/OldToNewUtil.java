package org.example.i18n.temp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.example.i18n.domain.dto.LoopFileParam;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import static org.example.i18n.consts.CommonConstant.chineseLP;
import static org.example.i18n.consts.CommonConstant.startChineseLP;

/**
 * MainController临时辅助类
 *
 * @author wangyichun
 * @since 2022/1/4 18:17
 */
public interface OldToNewUtil {

    static void format(LoopFileParam<Set<String>> param) {
        File source = param.getSources();
        // 判断文件是否需要类型过滤
        boolean pass = param.pass(source);
        if (pass) {
            return;
        }
        File target = param.getTarget();
        List<String> info = FileUtil.readLines(source, StandardCharsets.UTF_8);
        // 获取过滤关键字
        String keyWord = param.getKeyWords();
        String[] keyWords = keyWord.split(",");
        boolean longMark = false;
        for (String row : info) {
            String trimRow = row.trim();
            // 是否为空
            if (StrUtil.isBlankOrUndefined(trimRow)) {
                continue;
            }
            if (longMark) {
                longMark = !(trimRow.contains("*/"));
                continue;
            }
            if (trimRow.startsWith("/*")) {
                longMark = true;
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
            boolean matched = Arrays.stream(keyWords)
                    .filter(s -> !StrUtil.isBlankOrUndefined(s))
                    .anyMatch(s -> StrUtil.containsIgnoreCase(finalTrimRow, s.trim()));
            if (matched) {
                FileUtil.appendUtf8String(source.getName() + "=" + row + "\n", target);
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
