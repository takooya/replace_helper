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
public class LoopFile2MapParam extends CommonCompareBase {
    /**
     * 入参
     */
    @NotBlank(message = "目标地址不可以为空")
    private String sourcesPath;
    /**
     * 入参
     * 设置替换表达式
     * key: 匹配正则表达式
     * value: 替换正则表达式
     */
    @NotEmpty(message = "过滤正则不可以为空")
    private Map<String, String> regExpMap;
    /**
     * 中间变量
     * {@link LoopFile2MapParam#sourcesPath}对应的File对象
     */
    private File sources;
}
