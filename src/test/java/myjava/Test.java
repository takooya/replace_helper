package myjava;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.i18n.utils.DirectoryUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author wangyichun
 * @since 2021/12/30 17:16
 */
@Slf4j
public class Test {
    public static void main(String[] args) throws IOException {
        List<String> name = ListUtil.list(false,
                "tds-service-parent-pom",
                "sc-service-product",
                "tds-service-svc-pdi",
                "common-service-dynamic-datasource",
                "tds-service-svc-data",
                "tds-service-spa-manamasterdata",
                "tds-service-spa-manasal",
                "tds-service-spa-manapur",
                "tds-service-spa-masterdata",
                "tds-service-svc-bln",
                "tds-service-svc-flautspa",
                "tds-service-svc-clm",
                "tds-service-sal",
                "tds-service-spt",
                "tds-service-spa",
                "tds-service-svc",
                "common-service-quartz",
                "tds-service-app",
                "common-service-message",
                "tds-service-demo",
                "tds-sys",
                "base-start",
                "common-ui",
                "tds-cloud",
                "tds-config"
        );
        String prefix = "C:\\Users\\takooya\\idea_project\\count-feign\\";
        String suffix = "\\src\\main\\java\\com";
        File tempFile = File.createTempFile("temp", "txt");
        for (String s : name) {
            File source = new File(prefix + s + suffix);
            if (source.exists()) {
                DirectoryUtil.loopFiles(source, file -> {
                    AtomicReference<String> feignLine = new AtomicReference<>();
                    List<String> requestLine = new ArrayList<>();
                    List<String> lines = FileUtil.readLines(file, StandardCharsets.UTF_8);
                    boolean isFeign = lines.stream().anyMatch(line -> line.contains("@FeignClient"));
                    if (!isFeign) {
                        return;
                    }
                    lines.forEach(line -> {
                        if (line.contains("@FeignClient")) {
                            feignLine.set(line);
                        }
                        if (line.contains("@") && line.contains("Mapping")) {
                            requestLine.add(line);
                        }
                    });
                    requestLine.forEach(s1 -> FileUtil.appendUtf8String(
                            s1 + "#:" + feignLine.get() + "#:" + s + "\n\n"
                            , tempFile));
                });
            }
        }
        List<String> result = FileUtil.readLines(tempFile, StandardCharsets.UTF_8);
        log.info("[-Test-].main:result={}", result);
    }
}
