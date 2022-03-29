package org.example.i18n.domain.bases;

import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;

//    /**
//     * 接口变量
//     */
//    private String targetsType;
//    /**
//     * 接口变量
//     */
//    private String filtersType;
public interface TypeFilterRule {
    /**
     * 入参
     * 设置目标文件类型
     * 判断文件是否为目标文件类型,如果是目标文件类型,执行逻辑
     */
    String getTargetsType();

    /**
     * 入参
     * 设置目标文件类型
     * 判断文件是否为目标文件类型,如果是目标文件类型,执行逻辑
     */
    void setTargetsType(String targetsType);

    /**
     * 入参
     * 设置过滤文件类型
     * 判断文件是否为过滤类型,如果是过滤类型,跳过文件收集
     */
    String getFiltersType();

    /**
     * 入参
     * 设置过滤文件类型
     * 判断文件是否为过滤类型,如果是过滤类型,跳过文件收集
     */
    void setFiltersType(String filtersType);

    /**
     * 判断文件是否需要类型过滤
     *
     * @param name 文件名
     * @return 是否放行下面的逻辑
     */
    default boolean pass(String name) {
        if (StrUtil.isNotBlank(this.getFiltersType())) {
            String[] types = this.getFiltersType().trim().split(",");
            List<String> typeList = Arrays.asList(types);
            boolean filterMatch = typeList.stream().anyMatch(s -> name.contains("." + s));
            // 过滤类型被匹配, 要 pass文件
            return filterMatch;
        }
        if (StrUtil.isNotBlank(this.getTargetsType())) {
            String[] types = this.getTargetsType().trim().split(",");
            List<String> typeList = Arrays.asList(types);
            boolean targetMatch = typeList.stream().anyMatch(s -> name.contains("." + s));
            // 目标类型被匹配, 不要 pass文件
            return !targetMatch;
        }
        // 没有被匹配到,则 不要 pass文件
        return false;
    }

    /**
     * 判断文件是否需要类型过滤
     *
     * @param file 文件
     * @return 是否放行下面的逻辑
     */
    default boolean pass(File file) {
        String name = file.getAbsolutePath();
        return this.pass(name);
    }
}
