package myjava;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest {
    public static void main(String[] args) {
        String source1 = "  @Table(\n";
        String source2 = "          name = \"TS_ACCOUNT\"\n";
        String source3 = ")\n";
        String source4 = "  public Date getChangeDate() {";
        String source5 = "      return this.changeDate;";
        String source6 = "  )";

        Pattern pat = Pattern.compile("\\)");
        Matcher matcher1 = pat.matcher(source1.trim());
        Matcher matcher2 = pat.matcher(source2.trim());
        Matcher matcher3 = pat.matcher(source3.trim());
        Matcher matcher4 = pat.matcher(source4.trim());
        Matcher matcher5 = pat.matcher(source5.trim());
        Matcher matcher6 = pat.matcher(source6.trim());

        System.out.println("[-RegTest:main-matcher1]:=" + matcher1.matches());
        System.out.println("[-RegTest:main-matcher2]:=" + matcher2.matches());
        System.out.println("[-RegTest:main-matcher3]:=" + matcher3.matches());
        System.out.println("[-RegTest:main-matcher4]:=" + matcher4.matches());
        System.out.println("[-RegTest:main-matcher5]:=" + matcher5.matches());
        System.out.println("[-RegTest:main-matcher6]:=" + matcher6.matches());

//        String line = "            method = {RequestMethod.GET}";
//        Pattern pattern = Pattern.compile("method = \\{RequestMethod.\\[POST|GET]}");
//        Matcher matcher = pattern.matcher(line.trim());
//        System.out.println("[-RegTest:main-]:matcher=" + matcher.matches());

        String line = "            method = {RequestMethod.GET}";
        Pattern pattern = Pattern.compile("method = [{]RequestMethod.(POST|GET)[}]");
        Matcher matcher = pattern.matcher(line.trim());
        System.out.println("[-RegTest:main-]:matcher=" + matcher.matches());
    }
}
