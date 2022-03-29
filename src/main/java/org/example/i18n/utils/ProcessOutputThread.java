package org.example.i18n.utils;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProcessOutputThread extends Thread {
    private final InputStream is;
    @Getter
    private final List<String> outputList;

    public ProcessOutputThread(InputStream is) throws IOException {
        if (null == is) {
            throw new IOException("the provided InputStream is null");
        }
        this.is = is;
        this.outputList = new ArrayList<>();
    }

    @Override
    public void run() {
        try (InputStream curIs = this.is) {
            String read = IoUtil.read(curIs, StandardCharsets.UTF_8);
            this.outputList.add(read);
        } catch (IORuntimeException | IOException e) {
            e.printStackTrace();
        }
    }
}