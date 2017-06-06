package com.fueled.flowr.compilers;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Arrays;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

/**
 * Created by julienFueled on 5/25/17.
 */
public class DeepLinkAnnotationCompilerTest {

    private static final String TEST_PACKAGE = "com.fueled.flowr.sample";

    private static final JavaFileObject TEST_DEEP_LINK_FRAGMENT = JavaFileObjects
            .forSourceString("HomeFragment", "package " + TEST_PACKAGE + ";\n" +
                    "import com.fueled.flowr.AbstractFlowrFragment;\n" +
                    "import com.fueled.flowr.annotations.DeepLink;\n" +
                    "@DeepLink(value = {\"/test\"})\n" +
                    "public class HomeFragment extends AbstractFlowrFragment {\n" +
                    "}");

    private static final JavaFileObject TEST_DEEP_LINK_HANDLER = JavaFileObjects
            .forSourceString("TestDeepLinkHandler", "package " + TEST_PACKAGE + ";\n" +
                    "import com.fueled.flowr.annotations.DeepLinkHandler;\n" +
                    "@DeepLinkHandler\n" +
                    "public interface TestDeepLinkHandler {\n" +
                    "}");

    private static final JavaFileObject TEST_DEEP_GENERATED_HANDLER = JavaFileObjects
            .forSourceString("TestDeepLinkHandlerImpl", "package com.fueled.flowr.sample;\n" +
                    "import com.fueled.flowr.internal.AbstractFlowrDeepLinkHandler;\n" +
                    "public final class TestDeepLinkHandlerImpl extends AbstractFlowrDeepLinkHandler {\n" +
                    "  public TestDeepLinkHandlerImpl() {\n" +
                    "    addFragment(\"/test\", HomeFragment.class);\n" +
                    "  }\n" +
                    "}");


    @Test
    public void process() throws Exception {
        assertAbout(javaSources())
                .that(Arrays.asList(TEST_DEEP_LINK_FRAGMENT, TEST_DEEP_LINK_HANDLER))
                .processedWith(new DeepLinkAnnotationCompiler())
        .compilesWithoutError()
        .and()
        .generatesSources(TEST_DEEP_GENERATED_HANDLER);
    }

}