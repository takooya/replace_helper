package org.example.i18n.domain.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.example.i18n.domain.bases.CommonCompareBase;
import org.example.i18n.domain.bases.CommonLoopBase;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.util.Map;

/**
 * 循环文件工具类
 *
 * @author wangyichun
 * @since 2022/1/4 12:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class Multi2OneParam extends CommonCompareBase {
    /**
     * 入参
     */
    @NotBlank(message = "目标地址不可以为空")
    private String sourcesPath;
    /**
     * 入参
     * 设置正则匹配表达式
     */
    @NotEmpty(message = "正则匹配表达式不可以为空")
    private Map<String, String> sourceRegExp;

    /**
     * 入参
     * 设置正则替换表达式
     */
    @NotEmpty(message = "正则替换表达式不可以为空")
    private String targetRegExp;
    /**
     * 中间变量
     */
    private File source;
}
