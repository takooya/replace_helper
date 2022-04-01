package org.example.i18n.domain.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.example.i18n.domain.bases.CommonCompareBase;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class BlockAppendParam extends CommonCompareBase {
    /**
     * 入参
     * 检查还是改写
     */
    private boolean check;
    /**
     * 入参
     */
    @NotBlank(message = "资源地址不可以为空")
    private String sourcesPath;
    /**
     * 入参
     */
    @NotBlank(message = "目标地址不可以为空")
    private String targetsPath;
    /**
     * 入参
     * 设置替换表达式
     */
    @NotEmpty(message = "待匹配添加的字符串不可以为空")
    private String appendRegStr;
    /**
     * 入参
     * 可变参数名匹配正则
     */
    @NotEmpty(message = "可变参数名匹配正则不可以为空")
    private String keyMatchReg;
    /**
     * 入参
     * 块开始标识
     */
    @NotBlank(message = "块开始标识不可以为空")
    private String blockStartMark;
    /**
     * 入参
     * 块开始标识
     */
    @NotBlank(message = "块结束标识不可以为空")
    private String blockEndMark;
    /**
     * 入参
     * 代码相似度
     */
    @NotNull(message = "代码相似度不可以为空")
    @DecimalMin(value = "0.0", message = "代码相似度数据不合法")
    @DecimalMax(value = "1.0", message = "代码相似度数据不合法")
    private Double similar;
    /**
     * 中间变量
     */
    private File sources;
}
