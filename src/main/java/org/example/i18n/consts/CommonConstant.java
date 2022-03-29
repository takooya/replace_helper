package org.example.i18n.consts;

import java.util.regex.Pattern;

/**
 * 静态资源工具
 *
 * @author wangyichun
 * @since 2021/12/29 7:48
 */
public interface CommonConstant {
    // 标准properties文件路径
    String propertiesPath = "C:\\Users\\takooya\\idea_project\\demo\\src\\test\\resources\\result.properties";
    // 中文正则
    String chineseLS = "[\\u4e00-\\u9fa5]+";
    Pattern chineseLP = Pattern.compile(chineseLS);
    // 中文开头正则
    String startChineseLS = "^[\\u4e00-\\u9fa5].*";
    Pattern startChineseLP = Pattern.compile(startChineseLS);
    // 开头空白正则
    String spaceS = "^(\\s+)";
    Pattern spaceSP = Pattern.compile(spaceS);
    // 工程路径字段名称, 也是数据库字典表对应的parentCode
    String PROJECT_SOURCE_PATH_KEY = "sourcePath";
    String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String DATA_FORMAT = "yyyy-MM-dd";
}
