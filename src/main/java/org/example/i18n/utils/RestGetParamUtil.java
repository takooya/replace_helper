package org.example.i18n.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangyichun
 */
public interface RestGetParamUtil {
    /**
     * 解析get请求的参数
     *
     * @param getParam get请求参数
     * @return 待拼接的url
     * @author wangyichun
     */
    static String uriVariablesStr(Map<String, String> getParam) {
        if (CollUtil.isEmpty(getParam)) {
            return "";
        }
        String middle = getParam.keySet().stream().map(key -> {
            String value = getParam.get(key);
            return key + "=" + value;
        }).collect(Collectors.joining(","));
        return "?" + middle;
    }

    /**
     * 解析get请求的参数
     *
     * @param url get请求url 不必是标准url，只要参数部分符合get请求参数即可
     * @return 解析后的参数
     * @author wangyichun
     * @date 2021/8/16 16:15
     */
    static Map<String, Collection<String>> getAndDecodeQueries(String url) {
        if (StrUtil.isBlankOrUndefined(url)) {
            return new LinkedHashMap<>();
        }
        int i = url.indexOf("?");
        if (i == -1) {
            return new LinkedHashMap<>();
        }
        String queryLine = url.substring(i + 1);
        return parseAndDecodeQueries(queryLine);
    }

    /**
     * 源码：{@link feign.RequestTemplate}
     */
    static Map<String, Collection<String>> parseAndDecodeQueries(String queryLine) {
        Map<String, Collection<String>> map = new LinkedHashMap<>();
        if (StrUtil.isBlankOrUndefined(queryLine)) {
            return map;
        }
        if (queryLine.indexOf('&') == -1) {
            putKV(queryLine, map);
        } else {
            char[] chars = queryLine.toCharArray();
            int start = 0;
            int i = 0;
            for (; i < chars.length; i++) {
                if (chars[i] == '&') {
                    putKV(queryLine.substring(start, i), map);
                    start = i + 1;
                }
            }
            putKV(queryLine.substring(start, i), map);
        }
        return map;
    }

    /**
     * 源码：{@link feign.RequestTemplate}
     */
    static void putKV(String stringToParse, Map<String, Collection<String>> map) {
        String key;
        String value;
        // note that '=' can be a valid part of the value
        int firstEq = stringToParse.indexOf('=');
        if (firstEq == -1) {
            key = urlDecode(stringToParse);
            value = null;
        } else {
            key = urlDecode(stringToParse.substring(0, firstEq));
            value = urlDecode(stringToParse.substring(firstEq + 1));
        }
        Collection<String> values = map.containsKey(key) ? map.get(key) : new ArrayList<>();
        values.add(value);
        map.put(key, values);
    }

    /**
     * 源码：{@link feign.RequestTemplate}
     */
    static String urlDecode(String arg) {
        try {
            return URLDecoder.decode(arg, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
