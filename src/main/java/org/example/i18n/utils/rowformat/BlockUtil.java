package org.example.i18n.utils.rowformat;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.collections4.map.SingletonMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockUtil {
    public static Map<Integer, SingletonMap<String, String>> removeBlock(List<String> source) {
        Map<Integer, SingletonMap<String, String>> results = new HashMap<>(source.size());
        boolean isBlock = false;
        for (int i = 1; i <= source.size(); i++) {
            String s = source.get(i - 1);
            String trimed = s.trim();
            String result = null;
            // 空行
            if (StrUtil.isBlank(trimed)) {
                result = s;
            }
            // 块注释起始
            if (trimed.contains("/*")) {
                if (trimed.startsWith("/*")) {
                    isBlock = true;
                } else {
                    if (trimed.contains("//")) {
                        if (trimed.indexOf("//") < trimed.indexOf("/*")) {
                            //块注释不生效,保留行注释
                            result = s;
                        } else {
                            //块注释生效,不保留内容
                            String beforeBlock = trimed.substring(0, trimed.indexOf("/*"));
                            if (StrUtil.isNotBlank(beforeBlock.trim())) {
                                result = beforeBlock;
                            }
                            isBlock = true;
                        }
                    } else {
                        //块注释生效
                        String beforeBlock = trimed.substring(0, trimed.indexOf("/*"));
                        if (StrUtil.isNotBlank(beforeBlock.trim())) {
                            result = beforeBlock;
                        }
                        isBlock = true;
                    }
                }
            }
            // 块注释结束
            if (trimed.contains("*/")) {
                isBlock = false;
                String afterBlock = trimed.substring(trimed.indexOf("*/"));
                if (StrUtil.isNotBlank(afterBlock)) {
                    result = afterBlock;
                }
            }
            // 不在块注释内
            if (!isBlock) {
                results.put(i, new SingletonMap<>(s, s));
            } else {
                results.put(i, new SingletonMap<>(s, result));
            }
        }
        return results;
    }

    public static Map<Integer, SingletonMap<String, String>> keepBlock(List<String> source) {
        Map<Integer, SingletonMap<String, String>> results = new HashMap<>(source.size());
        for (int i = 1; i <= source.size(); i++) {
            String s = source.get(i - 1);
            results.put(i, new SingletonMap<>(s, s));
        }
        return results;
    }

    public static Map<Integer, SingletonMap<String, String>> deal(List<String> source, CodeTypeEnum codeType) {
        switch (codeType) {
            case REMOVE_BLOCK:
                return removeBlock(source);
            case KEEP_BLOCK:
                return keepBlock(source);
            default:
                throw new RuntimeException("未处理的逻辑类型");
        }
    }
}
