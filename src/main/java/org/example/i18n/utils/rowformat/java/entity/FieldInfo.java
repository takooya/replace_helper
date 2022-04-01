package org.example.i18n.utils.rowformat.java.entity;

import java.util.List;

public class FieldInfo {
     /*     */ /*     */
    private List<String> blockComment;
    private String lineComment;
    private String code;
    private String row;

    public FieldInfo(String row) {
        this.row = row;
    }

    public List<String> getBlockComment() {
        return blockComment;
    }

    public String getLineComment() {
        return lineComment;
    }

    public String getCode() {
        return code;
    }
}
