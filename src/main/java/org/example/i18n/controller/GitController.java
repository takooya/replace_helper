package org.example.i18n.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.i18n.domain.dto.GitContentDto;
import org.example.i18n.domain.dto.GitTreeInfoDto;
import org.example.i18n.temp.MainUtil;
import org.example.i18n.utils.GitUtil;
import org.example.i18n.utils.RestGetParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * git操作
 *
 * @author wangyichun
 * @since 2022/1/5 20:11
 */
@Slf4j
@RestController
@RequestMapping("/git")
public class GitController {
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/files")
    public HashMap<String, GitTreeInfoDto> files(@RequestHeader Map<String, String> header,
                                                 @RequestBody Map<String, String> bodyParam) throws URISyntaxException {
        GitUtil.checkAll(header);
        List<GitTreeInfoDto> parentTree = this.tree(header, bodyParam);
        HashMap<String, GitTreeInfoDto> collect = new HashMap<>(128);
        BiFunction<Map<String, String>, Map<String, String>, List<GitTreeInfoDto>> func = new BiFunction<Map<String, String>, Map<String, String>, List<GitTreeInfoDto>>() {
            @SneakyThrows
            @Override
            public List<GitTreeInfoDto> apply(Map<String, String> map, Map<String, String> map2) {
                return tree(map, map2);
            }
        };
        GitUtil.loop(parentTree, collect, header, func);
        return collect;
    }

    @PostMapping("/tree")
    public List<GitTreeInfoDto> tree(@RequestHeader Map<String, String> header,
                                     @RequestBody Map<String, String> bodyParam) throws URISyntaxException {
        Map<String, String> param = MapUtil.builder(header).putAll(bodyParam).build();
        GitUtil.checkAll(param);
        HttpEntity<Map<String, String>> entity = GitUtil.makeEntity(header);
        String url = "http://" + param.get("githost") + "/api/v4/projects/" + param.get("projectid") + "/repository/tree";
        String append = RestGetParamUtil.uriVariablesStr(bodyParam);
        url = url + append;
        ResponseEntity<byte[]> exchange = restTemplate.exchange(new URI(url), HttpMethod.GET, entity, byte[].class);
        return JSONUtil.toList(new String(exchange.getBody(), StandardCharsets.UTF_8), GitTreeInfoDto.class);
    }

    @PostMapping("/info")
    public GitContentDto info(@RequestHeader Map<String, String> header,
                              @RequestBody Map<String, String> bodyParam) throws URISyntaxException {
        GitUtil.checkAll(header);
        GitUtil.checkParam(bodyParam, "branch");
        GitUtil.checkParam(bodyParam, "path");
        String path = bodyParam.get("path")
                .replace("/", "%2F")
                .replace(".", "%2E");
        String branch = bodyParam.get("branch");
        String url = "http://" + header.get("githost") + "/api/v4/projects/" + header.get("projectid") + "/repository/files/" + path + "?ref=" + branch;
        HttpEntity<Map<String, String>> entity = GitUtil.makeEntity(header);
        ResponseEntity<GitContentDto> exchange = restTemplate.exchange(new URI(url), HttpMethod.GET, entity, GitContentDto.class);
        return exchange.getBody();
    }

    @PostMapping("/content")
    public String content(@RequestHeader Map<String, String> header,
                          @RequestBody Map<String, String> bodyParam) throws URISyntaxException {
        GitUtil.checkAll(header);
        GitUtil.checkParam(bodyParam, "branch");
        GitUtil.checkParam(bodyParam, "path");
        String path = bodyParam.get("path")
                .replace("/", "%2F")
                .replace(".", "%2E");
        String branch = bodyParam.get("branch");
        String url = "http://" + header.get("githost") + "/api/v4/projects/" + header.get("projectid") + "/repository/files/" + path + "?ref=" + branch;
        HttpEntity<Map<String, String>> entity = GitUtil.makeEntity(header);
        ResponseEntity<GitContentDto> exchange = restTemplate.exchange(new URI(url), HttpMethod.GET, entity, GitContentDto.class);
        return Base64.decodeStr(exchange.getBody().getContent(), StandardCharsets.UTF_8);
    }


    @PostMapping("/loop")
    public List<String> loop(@RequestHeader Map<String, String> header,
                             @RequestBody Map<String, String> bodyParam) throws URISyntaxException {
        HashMap<String, GitTreeInfoDto> files = this.files(header, bodyParam);
        Set<String> filepaths = files.keySet();
        filepaths = filepaths.stream().filter(s -> s.endsWith(".java")).collect(Collectors.toSet());
        return filepaths.stream().map(new Function<String, String>() {
            @SneakyThrows
            @Override
            public String apply(String s) {
                Map<String, String> path = MapUtil.builder(bodyParam).put("path", s).build();
                return content(header, path);
            }
        }).collect(Collectors.toList());
    }


    @PostMapping("/loopByCondition")
    public Map<String, List<String>> loopByCondition(@RequestHeader Map<String, String> header,
                                                     @RequestBody Map<String, String> bodyParam) throws URISyntaxException {
        HashMap<String, GitTreeInfoDto> files = this.files(header, bodyParam);
        Set<String> filepaths = files.keySet();
        filepaths = filepaths.stream().filter(s -> s.endsWith(".java")).collect(Collectors.toSet());
        return filepaths.stream()
                .collect(HashMap::new, new BiConsumer<HashMap<String, List<String>>, String>() {
                    @SneakyThrows
                    @Override
                    public void accept(HashMap<String, List<String>> hashMap, String s) {
                        Map<String, String> path = MapUtil.builder(bodyParam).put("path", s).build();
                        String content = content(header, path);
                        String[] fileLines = content.split("\n");
                        List<String> haveChineseLine = MainUtil.recordList(Arrays.asList(fileLines));
                        if (CollUtil.isNotEmpty(haveChineseLine)) {
                            hashMap.put(s, haveChineseLine);
                        }
                    }
                }, Map::putAll);
    }


}
