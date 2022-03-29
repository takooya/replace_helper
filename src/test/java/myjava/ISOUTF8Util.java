package myjava;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static cn.hutool.core.util.CharsetUtil.GBK;

public class ISOUTF8Util {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String source = "ÓÃ»§ÎÞÈ¨µÇÂ½";
        byte[] uft8bytes = source.getBytes(StandardCharsets.UTF_8);
        String gb2312 = new String(uft8bytes, StandardCharsets.ISO_8859_1);
        System.out.println("[-GB2312UTF8:main-]:gb2312=" + gb2312);


        byte[] gb2312bytes = source.getBytes(StandardCharsets.ISO_8859_1);
        String utf8 = new String(gb2312bytes, StandardCharsets.UTF_8);
        System.out.println("[-GB2312UTF8:main-]:utf8=" + utf8);


        byte[] gbkbytes = source.getBytes(GBK);
        String utf8fromGbk = new String(gbkbytes, StandardCharsets.UTF_8);
        System.out.println("[-GB2312UTF8:main-]:utf8fromGbk=" + utf8fromGbk);


        String gbk = new String(uft8bytes, GBK);
        System.out.println("[-GB2312UTF8:main-]:gbk=" + gbk);
    }
}
