package org.example.i18n.domain.param;

import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.example.i18n.domain.bases.CommonCompareBase;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 循环文件工具类
 *
 * @author wangyichun
 * @since 2022/1/4 12:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AddMethodCommentParam extends CommonCompareBase {
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
     * key: 匹配正则表达式
     * value: 替换正则表达式
     */
    @NotEmpty(message = "过滤正则不可以为空")
    private String regExp;
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
     * 处理数量限制
     */
    @NotNull(message = "处理数量限制不可以为空")
    private Integer limit;
    /**
     * 中间变量
     */
    private File sources;
    private List<JSONObject> results = new ArrayList<>();

    public void addResult(JSONObject resultJson) {
        this.results.add(resultJson);
    }
}
