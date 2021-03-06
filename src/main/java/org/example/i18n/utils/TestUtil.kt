//package org.example.i18n.utils
//
///**
// *
// *
// * @author wangyichun
// * @since 2022/1/18 13:09
// */
//fun getSimilarityRatio(str: String, target: String): Float {
//    val d: Array<IntArray> // 矩阵
//    val n = str.length
//    val m = target.length
//    var i: Int // 遍历str的
//    var j: Int // 遍历target的
//    var ch1: Char // str的
//    var ch2: Char // target的
//    var temp: Int // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
//    if (n == 0 || m == 0) {
//        return 0f
//    }
//    d = Array(n + 1) { IntArray(m + 1) }
//    i = 0
//    while (i <= n) {
//        // 初始化第一列
//        d[i][0] = i
//        i++
//    }
//    j = 0
//    while (j <= m) {
//        // 初始化第一行
//        d[0][j] = j
//        j++
//    }
//    i = 1
//    while (i <= n) {
//        // 遍历str
//        ch1 = str[i - 1]
//        // 去匹配target
//        j = 1
//        while (j <= m) {
//            ch2 = target[j - 1]
//            temp = if (ch1 == ch2 || ch1.toInt() == ch2.toInt() + 32 || ch1.toInt() + 32 == ch2.toInt()) {
//                0
//            } else {
//                1
//            }
//            // 左边+1,上边+1, 左上角+temp取最小
//            d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + temp)
//            j++
//        }
//        i++
//    }
//    return (1 - d[n][m].toFloat() / Math.max(str.length, target.length)) * 100f
//}