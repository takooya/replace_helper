package org.example.i18n.utils.rowformat;

import java.util.regex.Matcher;

public enum EqualTypeEnum {
    //    完全匹配
    EQUAL,
    //    包含
    CONTAIN,
    ;

    public boolean match(Matcher matcher) {
        switch (this) {
            case EQUAL:
                return matcher.matches();
            case CONTAIN:
                return matcher.find();
            default:
                throw new RuntimeException("未处理的逻辑!");
        }
    }
}
