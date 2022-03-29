package org.example.i18n.utils;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessExecutor {
    private ProcessExecutor() {
    }

    private Process process;

    @Getter
    private String success;
    @Getter
    private String error;

    public static ProcessExecutor getInstance(String commend) throws IOException {
        ProcessExecutor result = new ProcessExecutor();
        result.process = Runtime.getRuntime().exec(commend);
        ProcessOutputThread inputStream = new ProcessOutputThread(result.process.getInputStream());
        ProcessOutputThread errorStream = new ProcessOutputThread(result.process.getErrorStream());
        inputStream.start();
        errorStream.start();
        try {
            boolean b = result.process.waitFor(1, TimeUnit.SECONDS);
            if (!b) {
                // 执行超时
                result.error = "执行超时";
                return result;
            }
            inputStream.join();
            errorStream.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            // 发生异常
            result.error = "[ERROR]: " + e.getMessage();
            return result;
        }
        List<String> successList = inputStream.getOutputList();
        if (successList.size() > 1) {
            throw new RuntimeException("系统未知情况!");
        }
        result.success = successList.get(0);
        List<String> errorList = errorStream.getOutputList();
        if (errorList.size() > 1) {
            throw new RuntimeException("系统未知情况!");
        }
        result.error = errorList.get(0);
        return result;
    }
}