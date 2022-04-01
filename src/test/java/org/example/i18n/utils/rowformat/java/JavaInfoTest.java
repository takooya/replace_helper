package org.example.i18n.utils.rowformat.java;

import cn.hutool.core.util.ReUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class JavaInfoTest {

    @org.junit.Test
    public void classPattern() {
        List<String> classRow = Arrays.asList(
                "private class Ufdsaufdsa{",
                "private class Ufdsaufdsa {",
                "public class Ufdsaufdsa{",
                "public class Ufdsaufdsa {",
                "class Ufdsaufdsa{//fdsafdsa",
                "class Ufdsaufdsa {//fdsafdsa",
                "class Ufdsaufdsa{ //fdsafdsa",
                "class Ufdsaufdsa { //fdsafdsa"
        );
        String exp = "(private|public)*\\s*class\\s+[a-zA-Z0-9]+\\s*\\{.*";
        Pattern compile = Pattern.compile(exp);
        for (String s : classRow) {
            boolean match = ReUtil.isMatch(exp, s);
            log.info("\n" + s + "       :{}", match);
        }
    }

    @org.junit.Test
    public void quotationPart() {
        List<String> quoRow = Arrays.asList(
                "\"",
                "\\\"",
                "\\\\\""
        );
        String abc = " +fdsafdsafdsafdsa        ";
        String exp = "((\\\\){0}\"|(\\\\){2}\"|(\\\\){4}\"|(\\\\){6}\"|(\\\\){8}\")";
        Pattern compile = Pattern.compile(exp);
        for (String s : quoRow) {
            boolean match = ReUtil.isMatch(exp, s);
            log.info("\n" + s + "       :{}", match);
        }
        System.out.println("abc =" + abc);
    }
}