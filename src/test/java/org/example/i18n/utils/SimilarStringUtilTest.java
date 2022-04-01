package org.example.i18n.utils;

import cn.hutool.core.util.StrUtil;

public class SimilarStringUtilTest {

    @org.junit.Test
    public void getSimilarityRatio4() {
        double similarityRatio = StrUtil.similar(
                "} catch (Throwable ) { throw ClassInfo.unwrapThrowable(); }",
                "} catch (Throwable ) { throw ClassInfo.unwrapThrowable(); }");
        System.out.println("[-SimilarStringUtilTest:getSimilarityRatio-]:similarityRatio=" + similarityRatio);
    }

    @org.junit.Test
    public void getSimilarityRatio3() {
        double similarityRatio = StrUtil.similar(
                "} catch (Throwable t) { throw ClassInfo.unwrapThrowable(t); }",
                "} catch (Throwable var7) { throw ClassInfo.unwrapThrowable(var7); }");
        System.out.println("[-SimilarStringUtilTest:getSimilarityRatio-]:similarityRatio=" + similarityRatio);
    }

    @org.junit.Test
    public void getSimilarityRatio1() {
        float similarityRatio = SimilarStringUtil.getSimilarityRatio(
                "} catch (Throwable t) { throw ClassInfo.unwrapThrowable(t); }",
                "} catch (Throwable var7) { throw ClassInfo.unwrapThrowable(var7); }");
        System.out.println("[-SimilarStringUtilTest:getSimilarityRatio-]:similarityRatio=" + similarityRatio);
    }

    @org.junit.Test
    public void getSimilarityRatio2() {
        float similarityRatio = SimilarStringUtil.getSimilarityRatio(
                "} catch (Throwable ) { throw ClassInfo.unwrapThrowable(); }",
                "} catch (Throwable ) { throw ClassInfo.unwrapThrowable(); }");
        System.out.println("[-SimilarStringUtilTest:getSimilarityRatio-]:similarityRatio=" + similarityRatio);
    }

    @org.junit.Test
    public void getRepeatRatio() {
    }
}