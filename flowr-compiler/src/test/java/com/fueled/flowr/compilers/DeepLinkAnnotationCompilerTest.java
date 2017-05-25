package com.fueled.flowr.compilers;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by julienFueled on 5/25/17.
 */
public class DeepLinkAnnotationCompilerTest {

    private static final String TEST_PACKAGE = "com.fueled.flowr.sample";

    DeepLinkAnnotationCompiler compiler;

    @Before
    public void setup() {
        compiler = new DeepLinkAnnotationCompiler();
    }

    @Test
    public void process() throws Exception {

    }

    @Test
    public void generateCanonicalNameOneFragmentTest() throws Exception {
        List<String> fragmentList = new ArrayList<>();
        fragmentList.add("com.fueled.flowr.sample.DemoFragment");
        String handlerPackage = compiler.generateCanonicalName(fragmentList);
        assertEquals(handlerPackage, TEST_PACKAGE);
    }

    @Test
    public void generateCanonicalNameTwoFragmentSamePackageTest() throws Exception {
        List<String> fragmentList = new ArrayList<>();
        fragmentList.add(TEST_PACKAGE + ".DemoFragment");
        fragmentList.add(TEST_PACKAGE + ".TestFragment");
        String handlerPackage = compiler.generateCanonicalName(fragmentList);
        assertEquals(handlerPackage, TEST_PACKAGE);
    }

    @Test
    public void generateCanonicalNameTwoFragmentDifferentPackageTest() throws Exception {
        List<String> fragmentList = new ArrayList<>();
        fragmentList.add(TEST_PACKAGE + ".demo.DemoFragment");
        fragmentList.add(TEST_PACKAGE + ".TestFragment");
        String handlerPackage = compiler.generateCanonicalName(fragmentList);
        assertEquals(handlerPackage, TEST_PACKAGE);
    }

    @Test
    public void generateCanonicalNameTwoFragmentDifferentPackage2Test() throws Exception {
        List<String> fragmentList = new ArrayList<>();
        fragmentList.add(TEST_PACKAGE + ".demo.DemoFragment");
        fragmentList.add(TEST_PACKAGE + ".test.TestFragment");
        String handlerPackage = compiler.generateCanonicalName(fragmentList);
        assertEquals(handlerPackage, TEST_PACKAGE);
    }

}