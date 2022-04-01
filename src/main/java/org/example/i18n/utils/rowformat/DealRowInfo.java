package org.example.i18n.utils.rowformat;

import lombok.Data;

@Data
public class DealRowInfo {
    private String origin;
    private String dealed;
    private Integer rowNum;

    public DealRowInfo() {
    }

    public DealRowInfo(String origin, String dealed) {
        this.origin = origin;
        this.dealed = dealed;
    }

    public DealRowInfo(String origin, String dealed, Integer rowNum) {
        this.origin = origin;
        this.dealed = dealed;
        this.rowNum = rowNum;
    }

    public String onlyCode() {
        return LineUtil.removeComment(dealed).getDealed().trim();
    }

    public String onlyComment() {
        return LineUtil.getRowComment(dealed).getDealed().trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DealRowInfo that = (DealRowInfo) o;

        return origin.equals(that.origin);
    }

    @Override
    public int hashCode() {
        return origin.hashCode();
    }
}
