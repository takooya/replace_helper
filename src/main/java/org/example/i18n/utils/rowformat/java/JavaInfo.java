package org.example.i18n.utils.rowformat.java;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import org.example.i18n.utils.rowformat.java.entity.FieldInfo;
import org.example.i18n.utils.rowformat.java.enums.ClassVisitType;
import org.example.i18n.utils.rowformat.java.enums.JavaType;

import java.util.ArrayList;
import java.util.List;

import static org.example.i18n.utils.rowformat.java.consts.JavaConst.CLASS_PATTERN;
import static org.example.i18n.utils.rowformat.java.consts.JavaConst.IMPORT_PATTERN;
import static org.example.i18n.utils.rowformat.java.consts.JavaConst.PACKAGE_PATTERN;

public class JavaInfo {

    public JavaInfo(List<String> rows) {
        // package mark
        boolean pm = true;
        // import mark
        boolean im = true;
        // block comment mark
        boolean bcm = false;
        // line comment
        for (int i = 0; i < rows.size(); i++) {
            String row = rows.get(i);
            List<String> blocks = new ArrayList<>();
            if (row.contains("/*")) {
                // 不在双引号内
                int blockStartIndex = row.indexOf("/*");
                int lineCommentIndex = row.indexOf("//");
                int quotationIndex = row.indexOf("\"");
                int blockEndIndex = row.indexOf("*/");
                int minIndex = NumberUtil.min(blockStartIndex, lineCommentIndex, quotationIndex, blockEndIndex);
                if (minIndex == blockStartIndex) {
                    if (blockEndIndex != -1) {
                        blocks.add(row.trim());
                    } else {

                    }
                }
                // 不在//后
                // */在当前行出现
                if (row.contains("//")) {
                    if (row.indexOf("//") < row.indexOf("/*"));
                } else {

                }
                bcm = true;
            }
            if (pm) {
                boolean match = ReUtil.isMatch(PACKAGE_PATTERN, row);
                if (match) {
                    this.fullPackage = row;
                    pm = false;
                }
            }
            if (im) {
                boolean match = ReUtil.isMatch(IMPORT_PATTERN, row);
                if (match) {
                    this.fullImports.add(row);
                }
            }
            if (ReUtil.isMatch(CLASS_PATTERN, row)) {
                pm = false;
                im = false;
                this.visitType = ClassVisitType.find(row);
                this.javaType = JavaType.find(row);
                this.isStaticClass = row.contains(" static ");
            }
        }
    }

    /**
     * java文件类型,interface,class,enum等
     */
    private JavaType javaType;
    /**
     * 是否是静态类
     */
    private boolean isStaticClass;
    /**
     * 类访问类型
     */
    private ClassVisitType visitType;
    /**
     * package信息
     */
    private String fullPackage;
    /**
     * 所有import文件
     */
    private List<String> fullImports = new ArrayList<>();
    /**
     * 不包含内部类的代码
     */
    private List<String> privateClassBody = new ArrayList<>();
    /**
     *
     */
    private List<JavaInfo> innerClassRow = new ArrayList<>();

    private List<FieldInfo> fieldInfo;
}
