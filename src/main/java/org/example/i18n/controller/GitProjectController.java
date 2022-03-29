package org.example.i18n.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.i18n.domain.dto.GitProjectInfoDto;
import org.example.i18n.utils.GitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 工程信息查询
 *
 * @author wangyichun
 * @since 2022/1/10 11:36
 */
@Slf4j
@RestController
@RequestMapping("/git")
public class GitProjectController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GitController gitController;

    public static String PER_PAGE_STRING = "per_page=100";


    @PostMapping("/projectid")
    public JSONObject getProjectId(@RequestHeader Map<String, String> header,
                                   @RequestBody Map<String, String> bodyParam) throws URISyntaxException {
        Map<String, String> param = MapUtil.builder(header).putAll(bodyParam).build();
        GitUtil.checkParam(param, "githost");
        GitUtil.checkParam(param, "token");
        HttpEntity<Map<String, String>> entity = GitUtil.makeEntity(header);
        String path = bodyParam.get("path")
                .replace("/", "%2F")
                .replace(".", "%2E");

        String groups = header.get("groupid");
        if (StrUtil.isBlank(groups)) {
            groups = "";
        } else {
            groups = "groups/" + groups + "/";
        }

        String url = "http://" + header.get("githost") + "/api/v4/" + groups + "projects/" + path + "?" + PER_PAGE_STRING;
        ResponseEntity<byte[]> exchange = restTemplate.exchange(new URI(url), HttpMethod.GET, entity, byte[].class);
        return JSONUtil.parseObj(new String(exchange.getBody(), StandardCharsets.UTF_8), true);
    }

    @GetMapping("/projects")
    public List<GitProjectInfoDto> projects(@RequestHeader Map<String, String> header) throws URISyntaxException {
        GitUtil.checkParam(header, "githost");
        GitUtil.checkParam(header, "token");
        HttpEntity<Map<String, String>> entity = GitUtil.makeEntity(header);

        String groups = header.get("groupid");
        if (StrUtil.isBlank(groups)) {
            groups = "";
        } else {
            groups = "groups/" + groups + "/";
        }

        String namespace = header.get("namespace");
        if (StrUtil.isBlank(namespace)) {
            namespace = "?" + PER_PAGE_STRING;
        } else {
            namespace = "?search=" + namespace + "&search_namespaces=true" + "&" + PER_PAGE_STRING;
        }
        String url = "http://" + header.get("githost") + "/api/v4/" + groups + "projects/" + namespace;
        ResponseEntity<String> exchange = restTemplate.exchange(new URI(url), HttpMethod.GET, entity, String.class);
        String body = exchange.getBody();
        List<GitProjectInfoDto> result = JSONUtil.toList(body, GitProjectInfoDto.class);
        return result;
    }

    @GetMapping("/projects/{field}")
    public List projects_field(@RequestHeader Map<String, String> header,
                               @PathVariable("field") String field
    ) throws URISyntaxException, NoSuchFieldException {
        GitUtil.checkParam(header, "githost");
        GitUtil.checkParam(header, "token");
        HttpEntity<Map<String, String>> entity = GitUtil.makeEntity(header);

        String groups = header.get("groupid");
        if (StrUtil.isBlank(groups)) {
            groups = "";
        } else {
            groups = "groups/" + groups + "/";
        }
        String namespace = header.get("namespace");
        if (StrUtil.isBlank(namespace)) {
            namespace = "?" + PER_PAGE_STRING;
        } else {
            namespace = "?search=" + namespace + "&search_namespaces=true" + "&" + PER_PAGE_STRING;
        }
        String url = "http://" + header.get("githost") + "/api/v4/" + groups + "projects/" + namespace;
        ResponseEntity<String> exchange = restTemplate.exchange(new URI(url), HttpMethod.GET, entity, String.class);
        String body = exchange.getBody();
        List<GitProjectInfoDto> result = JSONUtil.toList(body, GitProjectInfoDto.class);
        Field selectField = GitProjectInfoDto.class.getDeclaredField(field);
        selectField.setAccessible(true);
        return result.stream().map(gitProjectInfoDto -> {
            try {
                return selectField.get(gitProjectInfoDto);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }


    @PostMapping("/projects/loopByCondition")
    public Map<String, Map> loopByCondition(@RequestHeader Map<String, String> header,
                                            @RequestBody Map<String, String> bodyParam) throws URISyntaxException {
        GitUtil.checkParam(bodyParam, "projects_filter");
        List<GitProjectInfoDto> projects = projects(header);
        Map<String, Map> result = new HashMap<>();
        String projectsFilter = bodyParam.get("projects_filter");
        String[] projectsArray = projectsFilter.split(",");
        for (GitProjectInfoDto project : projects) {
            boolean b = Stream.of(projectsArray).anyMatch(s -> project.getName_with_namespace().contains(s));
            if (!b) {
                continue;
            }
            Map<String, String> innerHeader = MapUtil.builder(header)
                    .put("projectid", String.valueOf(project.getId())).build();
            Map<String, List<String>> chineseFiles = gitController.loopByCondition(innerHeader, bodyParam);
            result.put(project.getName_with_namespace(), chineseFiles);
        }
        return result;
    }


    @GetMapping("/projects_temp")
    public List<String> projectsTemp(@RequestHeader Map<String, String> header) throws URISyntaxException {
        List<GitProjectInfoDto> result = this.projects(header);
        return result.stream().map(pinfo -> {
            String description = pinfo.getDescription();
            description = new String(description.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String git_url = pinfo.getHttp_url_to_repo();
            String unkonw_code = git_url.split("/")[2];
            git_url.replace(unkonw_code, header.get("githost"));
            return pinfo.getNamespace().getName() + "#" + pinfo.getName() + "#" + pinfo.getPath() + "#" +
                    description + "#" + git_url;
        }).collect(Collectors.toList());
    }
}
