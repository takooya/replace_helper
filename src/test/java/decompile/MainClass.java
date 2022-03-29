package decompile;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.i18n.utils.DirectoryUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class MainClass {
    static String jadPath = "C:\\Users\\Administrator\\IdeaProjects\\I18n\\src\\main\\resources\\jad.exe";
    static String sourceDir = "C:\\Users\\Administrator\\Desktop\\cinda_file\\code_update_record\\20220323-product_20220314package\\multi_dir\\jad_decompile\\itms_cinda";

    public static void main(String[] args) throws IOException {

        File source = new File(sourceDir);

//        String testCommend = "C:\\Users\\Administrator\\IdeaProjects\\I18n\\src\\main\\resources\\jad.exe -s java -o -p C:\\Users\\Administrator\\Desktop\\cinda_file\\code_update_record\\20220323-product_20220314package\\multi_dir\\jad_decompile\\itms_cinda\\WEB-INF\\classes\\ssoagent\\client\\filter\\AuthenFilter.class";
        String testCommend = "ping www.baidu.com";
        ProcessBuilder pb = new ProcessBuilder(testCommend);
        pb.redirectErrorStream(true);
        Process exec = pb.start();
        String readout = IoUtil.read(exec.getInputStream(), StandardCharsets.UTF_8);
        String error = IoUtil.read(exec.getErrorStream(), StandardCharsets.UTF_8);
        log.info("[-MainClass:main-]输出:{}", StrUtil.blankToDefault(readout, error));

        List<String> results = new ArrayList<>();
        List<String> errors = new ArrayList<>();

//        DirectoryUtil.loopFiles(source, (Consumer<File>) file -> {
//            String classPath = file.getAbsolutePath();
//            String commend;
//            if (!classPath.endsWith(".class")) {
//                return;
//            }
//            String javaPath = classPath.replace(".class", ".java");
//            commend = jadPath + " -s java -o -p " + classPath;
//            ProcessBuilder pbuilder = new ProcessBuilder(commend);
//            pbuilder.redirectErrorStream(true);
//            Process exec;
//            try {
//                exec = pbuilder.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return;
//            }
//            String execResult = IoUtil.read(exec.getInputStream(), StandardCharsets.UTF_8);
//            String errResult = IoUtil.read(exec.getErrorStream(), StandardCharsets.UTF_8);
//            if (StrUtil.isNotBlank(execResult)) {
//                // 同级文件夹写回java文件
//                FileUtil.writeBytes(execResult.getBytes(StandardCharsets.UTF_8), javaPath);
//                // 记录成功记录
//                results.add("[success]" + javaPath);
//            } else {
//                // 记录错误日志
//                errors.add("[ERROR : " + classPath + "]" + errResult);
//            }
//        });
    }
}
