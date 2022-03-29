package org.example.i18n.domain.bases;

public class TypeFilterBase implements TypeFilterRule {
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
