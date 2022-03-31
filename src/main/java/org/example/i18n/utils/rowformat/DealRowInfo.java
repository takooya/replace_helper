package org.example.i18n.utils.rowformat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealRowInfo {
    private String origin;
    private String dealed;

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
