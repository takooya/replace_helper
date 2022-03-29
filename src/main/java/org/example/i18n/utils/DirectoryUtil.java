package org.example.i18n.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import org.example.i18n.domain.param.LoopFile2MapParam;
import org.example.i18n.domain.param.LoopFileParam;

import java.io.File;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 文件夹工具
 *
 * @author wangyichun
 * @since 2021/12/29 7:47
 */
@SuppressWarnings("unused")
public interface DirectoryUtil {
    /**
     * 检查文件路径是否存在且是目录
     *
     * @param sourcePath 路径
     * @return 路径对应的File对象
     * @throws RuntimeException 目标地址不正确
     */
    static File isDir(String sourcePath) {
        if (CharSequenceUtil.isBlankOrUndefined(sourcePath)) {
            throw new RuntimeException("目标地址不正确");
        }
        File source = new File(sourcePath);
        if (!source.exists() || !source.isDirectory()) {
            throw new RuntimeException("目标地址不正确");
        }
        return source;
    }

    /**
     * 循环便利所有文件
     *
     * @param source   目录或文件
     * @param function 需要处理的文件函数(方法)
     */
    static void loopFiles(File source, Consumer<File> function) {
        if (source.isDirectory()) {
            List<File> files = FileUtil.loopFiles(source);

            for (File subFile : files) {
                loopFiles(subFile, function);
            }
        } else {
            function.accept(source);
        }
    }

    /**
     * 循环便利所有文件
     *
     * @param source   目标目录或文件
     * @param target   结果目录或文件
     * @param function 需要处理的文件函数(方法)
     */
    static void loopFiles(File source, File target, BiConsumer<File, File> function) {
        if (source.isDirectory()) {
            List<File> files = FileUtil.loopFiles(source);

            for (File subFile : files) {
                loopFiles(subFile, target, function);
            }
        } else {
            function.accept(source, target);
        }
    }

    /**
     * 循环便利所有文件
     *
     * @param param    便利处理文件时的关键参数
     * @param function 需要处理的文件函数(方法)
     */
    static void loopFiles(LoopFileParam param, Consumer<LoopFileParam> function) {
        File source = param.getSources();
        if (source.isDirectory()) {
            List<File> files = FileUtil.loopFiles(source);

            for (File subFile : files) {
                param.setSources(subFile);
                loopFiles(param, function);
            }
        } else {
            if (!param.pass(source)) {
                function.accept(param);
            }
        }
    }

    /**
     * 循环便利所有文件
     *
     * @param param    便利处理文件时的关键参数
     * @param function 需要处理的文件函数(方法)
     */
    static void loopFiles2Map(LoopFile2MapParam param, Consumer<LoopFile2MapParam> function) {
        File source = param.getSources();
        if (source.isDirectory()) {
            List<File> files = FileUtil.loopFiles(source);

            for (File subFile : files) {
                param.setSources(subFile);
                loopFiles2Map(param, function);
            }
        } else {
            function.accept(param);
        }
    }
}
