package org.example.i18n.utils.rowformat;

import org.apache.commons.collections4.map.SingletonMap;

import java.util.List;
import java.util.Map;

public enum CodeTypeEnum {
    // 空行
    REMOVE_BLANK,
    KEEP_BLANK,
    // 只有注释
    REMOVE_COMMENT,
    KEEP_COMMENT,
    // 只有代码
    TRIM_CODE,
    // 有注释,有代码
    KEEP_ORIGIN,
    // 块注释
    REMOVE_BLOCK,
    KEEP_BLOCK,
    // 行注释
    REMOVE_LINE_COMMENT,
    KEEP_LINE_COMMENT,
    ;

    public Map<Integer, SingletonMap<String, String>> deal(List<String> source) {
        switch (this) {
            case KEEP_BLOCK:
            case KEEP_COMMENT:
                return BlockUtil.keepBlock(source);
            case REMOVE_BLOCK:
            case REMOVE_COMMENT:
                return BlockUtil.removeBlock(source);
            default:
                throw new RuntimeException("未处理的场景!");
        }
    }
}
