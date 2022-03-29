package org.example.i18n.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.example.i18n.domain.dto.GitTreeInfoDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * git工具
 *
 * @author wangyichun
 * @since 2022/1/10 11:55
 */
public interface GitUtil {
    static void loop(List<GitTreeInfoDto> tree, Map<String, GitTreeInfoDto> collect,
                     Map<String, String> header, BiFunction<Map<String, String>, Map<String, String>, List<GitTreeInfoDto>> func) {
        for (GitTreeInfoDto o : tree) {
            String type = o.getType();
            if ("blob".equals(type)) {
                collect.put(o.getPath(), o);
            } else if ("tree".equals(type)) {
                Map<String, String> subParam = MapUtil.builder("path", o.getPath()).build();
                List<GitTreeInfoDto> subTree = func.apply(header, subParam);
                loop(subTree, collect, header, func);
            } else {
                throw new RuntimeException("未知类型");
            }
        }
    }

    static HttpEntity<Map<String, String>> makeEntity(Map<String, String> header) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String token = header.get("token");
        httpHeaders.set("PRIVATE-TOKEN", token);
        return new HttpEntity<>(MapUtil.empty(), httpHeaders);
    }

    static void checkParam(Map<String, String> param, String paramName) {
        if (StrUtil.isBlank(param.get(paramName))) {
            throw new RuntimeException(paramName + "不可以为空");
        }
    }

    static void checkAll(Map<String, String> param) {
        checkParam(param, "githost");
        checkParam(param, "projectid");
        checkParam(param, "token");
    }
}
