package com.fueled.flowr.compilers;

import com.fueled.flowr.annotations.DeepLink;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;


/**
 * Annotation processor that will generate the FlowrDeepLinkHandlerImpl based on the information provided by {@link com.fueled.flowr.annotations.DeepLink}.
 */
@SupportedAnnotationTypes("com.fueled.flowr.annotations.DeepLink")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class DeepLinkAnnotationCompiler extends AbstractProcessor {
    private static final String deeplinkFormat = "addFragment($S, (Class<? extends T>) $T.class)";
    private static final String HANDLER_FILE_NAME = "FlowrDeepLinkHandlerImpl";
    private static final String FLOWR_PACKAGE_NAME = "com.fueled.flowr";
    private static final String FLOWR_INTERNAL_PACKAGE_NAME = "com.fueled.flowr.internal";

    private static final TypeVariableName fragmentTypeVariableName = TypeVariableName.get("T", ClassName.get("android.support.v4.app", "Fragment"), ClassName.get(FLOWR_PACKAGE_NAME, "FlowrFragment"));
    private static final ParameterizedTypeName classExtendsFragmentTypeName = ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(fragmentTypeVariableName));
    private static final ClassName FlowrDeepLinkInfoClassName = ClassName.get(FLOWR_INTERNAL_PACKAGE_NAME, "FlowrDeepLinkInfo");

    private static final ClassName bundleClassName = ClassName.get("android.os", "Bundle");
    private static final ClassName uriClassName = ClassName.get("android.net", "Uri");
    private static final ParameterizedTypeName listStringTypeName = ParameterizedTypeName.get(List.class, String.class);

    /**
     * Check if a list of string contains the same substring.
     *
     * @param strings The list of String to search
     * @param pos     The position until where the character are the same.
     * @return
     */
    private static boolean allCharactersAreSame(List<String> strings, int pos) {
        String first = strings.get(0);
        for (String curString : strings) {
            if (curString.length() <= pos
                    || curString.charAt(pos) != first.charAt(pos)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Create the JavaPoet Type builder for the FlowrDeepLinkHandlerImpl class.
     *
     * @return The builder for the FlowrDeepLinkHandlerImpl class.
     */
    private static TypeSpec.Builder getClassObject() {
        return TypeSpec.classBuilder(HANDLER_FILE_NAME)
                .addTypeVariable(fragmentTypeVariableName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ClassName.get(FLOWR_INTERNAL_PACKAGE_NAME, "FlowrDeepLinkHandler"));
    }

    /**
     * Create the bundleUriInfo method of FlowrDeepLinkHandlerImpl class. This method extract the variables from a URL and add them to a Bundle.
     * The structure is <variable, value>.
     * For example the pattern in the DeepLink annotation \"/user/{userId}\", if the user access the app through \"/user/1234\", the function will return <userId,1234>.
     * Please note that all the variable will a string.
     *
     * @return a Bundle that contains the variables from the deep link.
     */
    private static MethodSpec bundleUriInfo() {
        return MethodSpec.methodBuilder("bundleUriInfo")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(bundleClassName)
                .addParameter(uriClassName, "uri")
                .addParameter(String.class, "pattern")
                .addCode("        String regex = getRegexPattern(pattern);\n" +
                        "        String path = uri.getPath();\n" +
                        "        $T m = Pattern.compile(regex).matcher(path);\n" +
                        "        if (m.matches()) {\n" +
                        "            $T data = new Bundle();\n" +
                        "            data.putString($T.DEEP_LINK_URL, uri.toString());\n" +
                        "            $T<String> params = getNamedGroupCandidates(regex).iterator();\n" +
                        "            //start at 1 because 0 is the searched string\n" +
                        "            int i = 1;\n" +
                        "            while (params.hasNext()) {\n" +
                        "                String bip = params.next();\n" +
                        "                data.putString(bip, m.group(i));\n" +
                        "                i++;\n" +
                        "            }\n" +
                        "            return data;\n" +
                        "        }\n" +
                        "        return null;", Matcher.class, bundleClassName, ClassName.get(FLOWR_PACKAGE_NAME, "Flowr"), Iterator.class)
                .build();

    }

    /**
     * Create the getRegexPattern method that generate the regular expression to extract variables form a URL.
     *
     * @return RegExp to extract variables form a URL.
     */
    private static MethodSpec getRegexPattern() {
        return MethodSpec.methodBuilder("getRegexPattern")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(String.class)
                .addParameter(String.class, "pattern")
                .addStatement("return pattern.replaceAll(\"\\\\{(.+?)\\\\}\", \"(?<$L>\\\\.+?)\")", "$1")
                .build();
    }

    /**
     * Create the getNamedGroupCandidates method. The method is used by {@link #bundleUriInfo()}
     *
     * @return A list of the variables in a pattern.
     */
    private static MethodSpec getNamedGroupCandidates() {
        return MethodSpec.methodBuilder("getNamedGroupCandidates")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(listStringTypeName)
                .addParameter(String.class, "regex")
                .addCode("$T namedGroups = new $T();\n" +
                        "\n" +
                        "        $T m = $T.compile(\"\\\\(\\\\?<([a-zA-Z][a-zA-Z0-9]*)>\").matcher(regex);\n" +
                        "\n" +
                        "        while (m.find()) {\n" +
                        "            $T test = m.group(1);\n" +
                        "            namedGroups.add(test);\n" +
                        "        }\n" +
                        "\n" +
                        "        return namedGroups;", listStringTypeName, ArrayList.class, Matcher.class, Pattern.class, String.class)
                .build();
    }

    /**
     * Create the routeIntentToScreen method. This method find the Fragment associated with the URL in the intent, extract the variables and set it as {@link com.fueled.flowr.Flowr#currentFragment}
     *
     * @return The routeIntentToScreen JavaPoet object.
     */
    private static MethodSpec routeIntentToScreen() {
        return MethodSpec.methodBuilder("routeIntentToScreen")
                .addModifiers(Modifier.PUBLIC)
                .returns(FlowrDeepLinkInfoClassName)
                .addParameter(ClassName.get("android.content", "Intent"), "intent")
                .addCode("$T uri = intent.getData();\n" +
                        "        if (uri != null) {\n" +
                        "            for (String uriPattern : linkFragmentMap.keySet()) {\n" +
                        "                $T data = bundleUriInfo(uri, uriPattern);\n" +
                        "                if (data != null) {\n" +
                        "                    return new $T(data, linkFragmentMap.get(uriPattern));\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "        return null;", uriClassName, bundleClassName, FlowrDeepLinkInfoClassName)
                .build();
    }

    /**
     * Create the constructor for FlowrDeepLinkHandlerImpl.
     *
     * @return The Constructor JavaPoet object.
     */
    private static MethodSpec.Builder generateConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("linkFragmentMap = new $T()", HashMap.class);
    }

    /**
     * Create addFragment method. This method add a Fragment associated with a deep link to the list of Fragment.
     *
     * @return The addFragment JavaPoet object.
     */
    private static MethodSpec addFragment() {
        return MethodSpec.methodBuilder("addFragment")
                .addModifiers(Modifier.PRIVATE)
                .returns(void.class)
                .addParameter(String.class, "url")
                .addParameter(classExtendsFragmentTypeName, "fragment")
                .addCode("linkFragmentMap.put(url, fragment);\n")
                .build();
    }

    /**
     * Create the linkFragmentMap field.
     *
     * @return The linkFragmentMap JavaPoet object.
     */
    private static FieldSpec linkFragmentMap() {
        return FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class), classExtendsFragmentTypeName), "linkFragmentMap")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build();
    }

    /**
     * Main method that will build the FlowrDeepLinkHandlerImpl.
     *
     * @param annotations the list of Annotations.
     * @param roundEnv    The environment object.
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("[FlowR]: generating deep Link Handler...");
        List<String> fragmentPathList = new ArrayList<>();
        MethodSpec.Builder constructorBuilder = generateConstructor();
        //browse all the annotations and create the FlowrDeepLinkHandler constructor
        for (Element element : roundEnv.getElementsAnnotatedWith(DeepLink.class)) {
            String objectType = element.asType().toString();
            String[] value = element.getAnnotation(DeepLink.class).value();
            fragmentPathList.add(objectType);
            for (String url : value) {
                constructorBuilder.addStatement(deeplinkFormat, url, ClassName.get(element.asType()));
            }

        }
        if (fragmentPathList.size() > 0) {
            final String handlerPackage = generateCanonicalName(fragmentPathList);
            generateDeepLinkHandler(handlerPackage, constructorBuilder);
            generateFlowrConfig(handlerPackage);

        }
        return true;
    }

    private void generateFlowrConfig(String handlerPackage) {
        TypeSpec config = TypeSpec.classBuilder("FlowrConfigImpl")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ClassName.get(FLOWR_INTERNAL_PACKAGE_NAME, "FlowrConfig"))
                .addMethod(MethodSpec.methodBuilder("getHandlerPackage")
                        .returns(String.class)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addStatement("return $S", handlerPackage + "." + HANDLER_FILE_NAME)
                        .build())
                .build();
        JavaFile javaFile = JavaFile.builder(FLOWR_PACKAGE_NAME, config)
                .build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateDeepLinkHandler(String handlerPackage, MethodSpec.Builder constructorBuilder) {
        TypeSpec classObject = getClassObject()
                .addField(linkFragmentMap())
                .addMethod(constructorBuilder.build())
                .addMethod(bundleUriInfo())
                .addMethod(getRegexPattern())
                .addMethod(getNamedGroupCandidates())
                .addMethod(addFragment())
                .addMethod(routeIntentToScreen())
                .build();

        JavaFile javaFile = JavaFile.builder(handlerPackage, classObject)
                .build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compute the best package to generate FlowrDeepLinkHandlerImpl in.
     *
     * @param fragmentPathList list of the fragment that support deep link.
     * @return The best package to generate FlowrDeepLinkHandlerImpl in.
     */
    protected String generateCanonicalName(List<String> fragmentPathList) {
        String packagePath;
        if (fragmentPathList.size() > 1) {
            int commonPrefixLength = 0;
            while (allCharactersAreSame(fragmentPathList, commonPrefixLength)) {
                commonPrefixLength++;
            }
            packagePath = fragmentPathList.get(0).substring(0, commonPrefixLength);
        } else {
            packagePath = fragmentPathList.get(0).replaceFirst("\\.([A-Za-z]+)$", "");
        }
        return packagePath.replaceAll("\\.$", "");
    }
}
