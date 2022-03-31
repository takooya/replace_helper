package org.example.i18n.utils.rowformat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public class LineUtil {
    public static DealRowInfo getRowComment(String row) {
        String trimRow = row.trim();
        if (trimRow.startsWith("//")) {
            return new DealRowInfo(row, trimRow);
        }
        if (trimRow.contains("//")) {
            String comment = trimRow.substring(trimRow.indexOf("//"));
            return new DealRowInfo(row, comment);
        } else {
            return new DealRowInfo(row, "");
        }
    }

    public static DealRowInfo getRowComment(DealRowInfo row) {
        String trimRow = row.getDealed().trim();
        if (trimRow.startsWith("//")) {
            return new DealRowInfo(row.getOrigin(), trimRow);
        }
        if (trimRow.contains("//")) {
            String comment = trimRow.substring(trimRow.indexOf("//"));
            return new DealRowInfo(row.getOrigin(), comment);
        } else {
            return new DealRowInfo(row.getOrigin(), "");
        }
    }

    public static Map<Integer, DealRowInfo> getRowComment(Map<Integer, DealRowInfo> infoMap) {
        Map<Integer, DealRowInfo> results = new HashMap<>();
        for (Integer rowNum : infoMap.keySet()) {
            DealRowInfo rowInfo = infoMap.get(rowNum);
            String row = rowInfo.getOrigin();
            String value = rowInfo.getDealed();
            DealRowInfo commentInfo = getRowComment(value);
            String comment = commentInfo.getDealed();
            results.put(rowNum, new DealRowInfo(row, comment));
        }
        return results;
    }

    public static Map<Integer, DealRowInfo> getRowComment(List<String> rows) {
        Map<Integer, DealRowInfo> results = new HashMap<>();
        for (int rowNum = 1; rowNum <= rows.size(); rowNum++) {
            String row = rows.get(rowNum - 1);
            DealRowInfo commentInfo = getRowComment(row);
            String comment = commentInfo.getDealed();
            results.put(rowNum, new DealRowInfo(row, comment));
        }
        return results;
    }

    public static DealRowInfo removeComment(String row) {
        String trimRow = row.trim();
        if (trimRow.startsWith("//")) {
            return new DealRowInfo(row, "");
        }
        if (trimRow.contains("//")) {
            String code = trimRow.substring(0, trimRow.indexOf("//"));
            return new DealRowInfo(row, code);
        } else {
            return new DealRowInfo(row, row);
        }
    }

    public static DealRowInfo removeComment(DealRowInfo row) {
        String trimRow = row.getDealed().trim();
        if (trimRow.startsWith("//")) {
            return new DealRowInfo(row.getOrigin(), "");
        }
        if (trimRow.contains("//")) {
            String code = trimRow.substring(0, trimRow.indexOf("//"));
            return new DealRowInfo(row.getOrigin(), code);
        } else {
            return new DealRowInfo(row.getOrigin(), row.getOrigin());
        }
    }

    public static Map<Integer, DealRowInfo> removeComment(Map<Integer, DealRowInfo> infoMap) {
        Map<Integer, DealRowInfo> results = new HashMap<>();
        for (Integer rowNum : infoMap.keySet()) {
            DealRowInfo rowInfo = infoMap.get(rowNum);
            String row = rowInfo.getOrigin();
            String value = rowInfo.getDealed();
            DealRowInfo codeInfo = removeComment(value);
            String code = codeInfo.getDealed();
            results.put(rowNum, new DealRowInfo(row, code));
        }
        return results;
    }

    public static Map<Integer, DealRowInfo> removeComment(List<String> rows) {
        Map<Integer, DealRowInfo> results = new HashMap<>();
        for (int rowNum = 1; rowNum <= rows.size(); rowNum++) {
            String row = rows.get(rowNum - 1);
            DealRowInfo codeInfo = removeComment(row);
            String code = codeInfo.getDealed();
            results.put(rowNum, new DealRowInfo(row, code));
        }
        return results;
    }
}
