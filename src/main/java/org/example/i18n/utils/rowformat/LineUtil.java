package org.example.i18n.utils.rowformat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public class LineUtil {
    public static DealInfo getRowComment(String row) {
        String trimRow = row.trim();
        if (trimRow.startsWith("//")) {
            return new DealInfo(row, trimRow);
        }
        if (trimRow.contains("//")) {
            String comment = trimRow.substring(trimRow.indexOf("//"));
            return new DealInfo(row, comment);
        } else {
            return new DealInfo(row, "");
        }
    }

    public static Map<Integer, DealInfo> getRowComment(Map<Integer, DealInfo> infoMap) {
        Map<Integer, DealInfo> results = new HashMap<>();
        for (Integer rowNum : infoMap.keySet()) {
            DealInfo rowInfo = infoMap.get(rowNum);
            String row = rowInfo.getOrigin();
            String value = rowInfo.getDealed();
            DealInfo commentInfo = getRowComment(value);
            String comment = commentInfo.getDealed();
            results.put(rowNum, new DealInfo(row, comment));
        }
        return results;
    }

    public static Map<Integer, DealInfo> getRowComment(List<String> rows) {
        Map<Integer, DealInfo> results = new HashMap<>();
        for (int rowNum = 1; rowNum <= rows.size(); rowNum++) {
            String row = rows.get(rowNum - 1);
            DealInfo commentInfo = getRowComment(row);
            String comment = commentInfo.getDealed();
            results.put(rowNum, new DealInfo(row, comment));
        }
        return results;
    }

    public static DealInfo removeComment(String row) {
        String trimRow = row.trim();
        if (trimRow.startsWith("//")) {
            return new DealInfo(row, "");
        }
        if (trimRow.contains("//")) {
            String code = trimRow.substring(0, trimRow.indexOf("//"));
            return new DealInfo(row, code);
        } else {
            return new DealInfo(row, row);
        }
    }

    public static Map<Integer, DealInfo> removeComment(Map<Integer, DealInfo> infoMap) {
        Map<Integer, DealInfo> results = new HashMap<>();
        for (Integer rowNum : infoMap.keySet()) {
            DealInfo rowInfo = infoMap.get(rowNum);
            String row = rowInfo.getOrigin();
            String value = rowInfo.getDealed();
            DealInfo codeInfo = removeComment(value);
            String code = codeInfo.getDealed();
            results.put(rowNum, new DealInfo(row, code));
        }
        return results;
    }

    public static Map<Integer, DealInfo> removeComment(List<String> rows) {
        Map<Integer, DealInfo> results = new HashMap<>();
        for (int rowNum = 1; rowNum <= rows.size(); rowNum++) {
            String row = rows.get(rowNum - 1);
            DealInfo codeInfo = removeComment(row);
            String code = codeInfo.getDealed();
            results.put(rowNum, new DealInfo(row, code));
        }
        return results;
    }
}
