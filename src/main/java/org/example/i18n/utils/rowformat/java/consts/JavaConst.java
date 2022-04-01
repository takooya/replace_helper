package org.example.i18n.utils.rowformat.java.consts;

import java.util.regex.Pattern;

public interface JavaConst {
    Pattern CLASS_PATTERN = Pattern.compile("(private|public)*\\s*class\\s+[a-zA-Z0-9]+\\s*\\{.*");
    Pattern PACKAGE_PATTERN = Pattern.compile("package .*");
    Pattern IMPORT_PATTERN = Pattern.compile("import .*");
    Pattern AVAIL_QUOT_PATTERN = Pattern.compile("((\\\\){0}\"|(\\\\){2}\"|(\\\\){4}\"|(\\\\){6}\"|(\\\\){8}\")");
}
