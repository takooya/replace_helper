package org.example.i18n.utils.rowformat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum CodeTypeEnum {
    // 保持原样
    KEEP_ORIGIN,
    // 去除空行
    REMOVE_BLANK,
    // 只去除块注释
    REMOVE_BLOCK,
    // 只去除行注释
    REMOVE_LINE_COMMENT,
    // 去除所有注释,只包含有效代码
    REMOVE_COMMENT,
    // 仅保留行注释
    KEEP_LINE_COMMENT,
    ;

    public Map<Integer, DealRowInfo> deal(List<String> source) {
        switch (this) {
            case KEEP_ORIGIN:
                return FileAllUtil.keepOrigin(source);
            case REMOVE_BLANK:
                return FileAllUtil.removeBlank(source);
            case REMOVE_BLOCK:
                return BlockUtil.removeBlock(source);
            case REMOVE_LINE_COMMENT:
                Map<Integer, DealRowInfo> temp1 = FileAllUtil.keepOrigin(source);
                return temp1.keySet().stream().collect(Collectors.toMap(
                        rowNum -> rowNum,
                        rowNum -> {
                            DealRowInfo dealRowInfo = temp1.get(rowNum);
                            return LineUtil.removeComment(dealRowInfo);
                        }
                ));
            case REMOVE_COMMENT:
                Map<Integer, DealRowInfo> temp = BlockUtil.removeBlock(source);
                return temp.keySet().stream().collect(Collectors.toMap(
                        rowNum -> rowNum,
                        rowNum -> {
                            DealRowInfo dealRowInfo = temp.get(rowNum);
                            return LineUtil.removeComment(dealRowInfo);
                        }
                ));
            case KEEP_LINE_COMMENT:
                Map<Integer, DealRowInfo> temp2 = BlockUtil.removeBlock(source);
                return temp2.keySet().stream().collect(Collectors.toMap(
                        rowNum -> rowNum,
                        rowNum -> {
                            DealRowInfo dealRowInfo = temp2.get(rowNum);
                            return LineUtil.getRowComment(dealRowInfo);
                        }
                ));
            default:
                throw new RuntimeException("未处理的场景!");
        }
    }
}
