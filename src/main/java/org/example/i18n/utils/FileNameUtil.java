package org.example.i18n.utils;

import cn.hutool.core.util.StrUtil;

import java.io.File;

/**
 * @author Administrator
 */
@SuppressWarnings("unused")
public interface FileNameUtil {
    /**
     * 获取文件后缀名
     *
     * @param file 文件对象
     * @return 文件后缀名
     */
    static String getFileSuffix(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 对比字符串,不区分大小写
     *
     * @param source 字符串
     * @param target 字符串
     * @return true: 相同; false: 不同
     */
    static boolean suffixCompare(String source, String target) {
        if (StrUtil.isBlankOrUndefined(source) || StrUtil.isBlankOrUndefined(target)) {
            return false;
        }
        return StrUtil.equalsIgnoreCase(source.trim(), target.trim());
    }
}
