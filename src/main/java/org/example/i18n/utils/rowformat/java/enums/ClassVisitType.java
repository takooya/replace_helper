package org.example.i18n.utils.rowformat.java.enums;

public enum ClassVisitType {
    PUBLIC,
    PRIVATE,
    NONE,
    ;

    public static ClassVisitType find(String arg) {
        if (arg.trim().startsWith("class") || arg.trim().startsWith("static")) {
            return NONE;
        } else if (arg.trim().startsWith("public")) {
            return PUBLIC;
        } else if (arg.trim().startsWith("private")) {
            return PRIVATE;
        }
        throw new RuntimeException("未知的java访问类型");
    }
}
