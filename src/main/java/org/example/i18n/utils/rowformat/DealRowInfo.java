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

}
