package org.example.i18n.domain.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.example.i18n.domain.bases.CommonLoopBase;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
public class ThisAppendParam extends CommonLoopBase {
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
     * 中间变量
     */
    private File sources;
}
