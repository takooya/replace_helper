package org.example.i18n.domain.bases;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.i18n.utils.rowformat.CodeTypeEnum;
import org.example.i18n.utils.rowformat.EqualTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Accessors(chain = true)
public class CommonLoopBase implements TypeFilterRule, ReplaceInfoRule {
    @Getter
    @Setter
    private boolean check = true;
    @Setter
    @Getter
    @NotNull(message = "代码过滤类型不可以为空")
    private CodeTypeEnum codeType;
    @Setter
    @Getter
    @NotNull(message = "代码比较类型不可以为空")
    private EqualTypeEnum equalType;
    /**
     * 接口变量
     * 输出变量
     */
    private List<ReplaceInfo> replaceInfos;

    @Override
    public List<ReplaceInfo> getReplaceInfos() {
        return replaceInfos;
    }

    @Override
    public void setReplaceInfos(List<ReplaceInfo> replaceInfos) {
        this.replaceInfos = replaceInfos;
    }

    /**
     * 接口变量
     */
    private String targetsType;
    /**
     * 接口变量
     */
    private String filtersType;

    @Override
    public String getTargetsType() {
        return targetsType;
    }

    @Override
    public void setTargetsType(String targetsType) {
        this.targetsType = targetsType;
    }

    @Override
    public String getFiltersType() {
        return filtersType;
    }

    @Override
    public void setFiltersType(String filtersType) {
        this.filtersType = filtersType;
    }
}
