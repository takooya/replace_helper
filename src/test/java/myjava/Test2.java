package myjava;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.sun.deploy.nativesandbox.IntegrityProcess;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wangyichun
 * @since 2021/12/30 17:16
 */
@Slf4j
public class Test2 {
    public static void main(String[] args) {
        String[] source = new String[]{
                "bindings",
                "core-js",
                "electron-find",
                "electron-store",
                "element-ui",
                "jquery",
                "node-addon-api",
                "node-sass",
                "sass-loader",
                "stylus",
                "stylus-loader",
                "vue",
                "vue-cron",
                "vue-lazyload",
                "vue-router",
                "vuex",
                "x2js"
        };
        List<String> result = Arrays.stream(source)
                .map(s -> {
                    String commend = "C:\\Program Files\\nodejs\\npm.cmd view " + s + " versions --json";
                    try {
                        Process exec = Runtime.getRuntime().exec(commend);
                        return s + ": " + IoUtil.read(exec.getInputStream(), StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        return "error: " + s + "\n";
                    }
                }).collect(Collectors.toList());

        File file = new File("C:\\Users\\Administrator\\WebstormProjects\\electron-vue-dev\\version_info.txt");
        FileUtil.writeLines(result, file, StandardCharsets.UTF_8);
    }
}
