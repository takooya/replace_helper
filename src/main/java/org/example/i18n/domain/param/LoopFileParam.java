package org.example.i18n.domain.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.example.i18n.domain.bases.TypeFilterBase;
import org.example.i18n.domain.bases.TypeFilterRule;

import java.io.File;

/**
 * 循环文件工具类
 *
 * @author wangyichun
 * @since 2022/1/4 12:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LoopFileParam<T> extends TypeFilterBase {
    /**
     * 入参
     * 资源路径
     */
    private String sourcesPath;
    /**
     * 入参
     * 关键字
     */
    private String keyWords;
    /**
     * 中间变量
     * 资源
     */
    private File sources;
    /**
     * 中间变量
     * 目标
     */
    private File target;
    private T data;
}
