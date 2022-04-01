package org.example.i18n.utils.rowformat.java.enums;

public enum JavaType {
    INTERFACE,
    CLASS,
    ENUM,
    ANNOTATION,
    ;

    public static JavaType find(String row) {
        if (row.contains(" @interface ")) {
            return ANNOTATION;
        } else if (row.contains(" interface ")) {
            return INTERFACE;
        } else if (row.contains(" class ")) {
            return CLASS;
        } else if (row.contains(" enum ")) {
            return ENUM;
        }
        throw new RuntimeException("未知的java文件类型");
    }
}
