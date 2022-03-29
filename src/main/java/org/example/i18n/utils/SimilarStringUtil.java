package org.example.i18n.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * 字符串相似度比较工具
 *
 * @author wangyichun
 * @since 2021/12/30 16:53
 */
public interface SimilarStringUtil {
    static float getSimilarityRatio(String str, String target) {
        int[][] d; // 矩阵
        int n = str.length();
        int m = target.length();
        int i; // 遍历str的
        int j; // 遍历target的
        char ch1; // str的
        char ch2; // target的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0 || m == 0) {
            return 0;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++) { // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + temp);
            }
        }
        return (1 - (float) d[n][m] / Math.max(str.length(), target.length())) * 100F;
    }

    static float getRepeatRatio(String str1, String str2) {
        int n = str1.length();
        int m = str2.length();
        if (n == 0 || m == 0) {
            return 0;
        }

        char[] char1 = str1.toCharArray();
        Set<Character> charSet1 = new HashSet<>(char1.length);
        for (char c : char1) {
            charSet1.add(c);
        }
        char[] char2 = str2.toCharArray();
        Set<Character> charSet2 = new HashSet<>(char2.length);
        for (char c : char2) {
            charSet2.add(c);
        }

        int norepeatSize1 = charSet1.size();
        int norepeatSize2 = charSet2.size();
        Set<Character> repeatSet = new HashSet<>(Math.max(norepeatSize1, norepeatSize2));
        repeatSet.addAll(charSet1);
        repeatSet.retainAll(charSet2);

        charSet1.removeAll(repeatSet);
        float ratio1 = (float) (norepeatSize1 - charSet1.size()) / (float) norepeatSize1;

        charSet2.removeAll(repeatSet);
        float ratio2 = (float) (norepeatSize2 - charSet2.size()) / (float) norepeatSize2;

        return Math.max(ratio1, ratio2);
    }
}
