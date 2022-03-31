package org.example.i18n.utils.rowformat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileAllUtil {
    public static Map<Integer, DealRowInfo> removeBlank(List<String> source) {
        Map<Integer, DealRowInfo> results = new HashMap<>(source.size());
        for (int i = 1; i <= source.size(); i++) {
            String s = source.get(i - 1);
            results.put(i, new DealRowInfo(s, s.trim()));
        }
        return results;
    }

    public static Map<Integer, DealRowInfo> keepOrigin(List<String> source) {
        Map<Integer, DealRowInfo> results = new HashMap<>(source.size());
        for (int i = 1; i <= source.size(); i++) {
            String s = source.get(i - 1);
            results.put(i, new DealRowInfo(s, s));
        }
        return results;
    }
}
