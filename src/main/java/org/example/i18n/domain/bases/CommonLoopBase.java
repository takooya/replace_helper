package org.example.i18n.domain.bases;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
public class CommonLoopBase implements TypeFilterRule, ReplaceInfoRule {
    @Getter
    @Setter
    private boolean check = true;
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
