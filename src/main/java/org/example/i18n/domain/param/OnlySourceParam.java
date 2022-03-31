package org.example.i18n.domain.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.example.i18n.domain.bases.CommonCompareBase;
import org.example.i18n.domain.bases.CommonLoopBase;

import javax.validation.constraints.NotBlank;

/**
 * 循环文件工具类
 *
 * @author wangyichun
 * @since 2022/1/4 12:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OnlySourceParam extends CommonCompareBase {
    /**
     * 入参
     * 资源路径
     */
    @NotBlank(message = "目标地址不可以为空")
    private String sourcesPath;
}
