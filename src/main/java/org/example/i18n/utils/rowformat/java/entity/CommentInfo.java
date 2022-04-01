package org.example.i18n.utils.rowformat.java.entity;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

import static org.example.i18n.utils.rowformat.java.consts.JavaConst.AVAIL_QUOT_PATTERN;

public class CommentInfo {
    @Getter
    private String row;
    @Getter
    private String blockComments;
    @Getter
    private String lineComment;
    @Getter
    private String quotationPart;
    @Getter
    private boolean blockFinish;
    @Getter
    private boolean isblock;
    @Getter
    private String code;

    public CommentInfo(String row) {
        this.row = row;
        String[] splitQuot = AVAIL_QUOT_PATTERN.split(row);
        List<Boolean> bl = Arrays.asList(
                splitQuot.length > 1,
                row.contains("//"),
                row.contains("/*"),
                row.contains("*/"));
        long count = bl.stream().filter(b -> b).count();
        if (count > 2) {
            throw new RuntimeException("此句过于复杂,请人工处理");
        }
        if (count == 1) {
            if (row.contains("//")) {
                this.blockComments = "";
                this.quotationPart = "";
                this.code = row.substring(0, row.indexOf("//")).trim();
                this.lineComment = row.substring(row.indexOf("//")).trim();
                this.blockFinish = false;
                this.isblock = false;
            } else if (row.contains("/*")) {
                this.blockComments = row.substring(row.indexOf("/*")).trim();
                this.quotationPart = "";
                this.code = row.substring(0, row.indexOf("/*")).trim();
                this.lineComment = "";
                this.blockFinish = false;
                this.isblock = true;
            } else if (row.contains("*/")) {
                this.blockComments = row.substring(0, row.indexOf("*/")).trim();
                this.quotationPart = "";
                this.code = row.substring(row.indexOf("*/")).trim();
                this.lineComment = "";
                this.blockFinish = true;
                this.isblock = true;
            }
        }
    }
}
