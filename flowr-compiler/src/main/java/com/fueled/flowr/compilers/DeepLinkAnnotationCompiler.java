package com.fueled.flowr.compilers;

import com.fueled.flowr.annotations.DeepLink;
import com.fueled.flowr.annotations.DeepLinkHandler;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;


/**
 * Annotation processor that will generate the FlowrDeepLinkHandlerImpl based on the information
 * provided by {@link DeepLink} and {@link DeepLinkHandler}.
 */
@SupportedAnnotationTypes({"com.fueled.flowr.annotations.DeepLink",
        "com.fueled.flowr.annotations.DeepLinkHandler"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public class DeepLinkAnnotationCompiler extends AbstractProcessor {

    private static final String deeplinkFormat = "addFragment($S, $T.class)";
    private static final String HANDLER_FILE_NAME_POST_FIX = "Impl";
    private static final String FLOWR_INTERNAL_PACKAGE_NAME = "com.fueled.flowr.internal";
    private static final String ABSTRACT_HANDLER_CLASS_NAME = "AbstractFlowrDeepLinkHandler";

    /**
     * Main method that will build the deep link handler implementation class.
     *
     * @param annotations the list of Annotations.
     * @param roundEnv    The environment object.
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("[FlowR]: generating deep Link Handler...");

        MethodSpec.Builder constructorBuilder = generateConstructor();

        // Browse all the annotations and create the FlowrDeepLinkHandler constructor
        for (Element element : roundEnv.getElementsAnnotatedWith(DeepLink.class)) {
            String[] value = element.getAnnotation(DeepLink.class).value();

            for (String url : value) {
                constructorBuilder.addStatement(deeplinkFormat, url, ClassName.get(element.asType()));
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(DeepLinkHandler.class)) {
            String packageName = processingEnv.getElementUtils().getPackageOf(element)
                    .getQualifiedName().toString();
            String className = element.getSimpleName().toString();

            generateDeepLinkHandler(packageName, className, constructorBuilder);
        }

        return true;
    }

    /**
     * Create the JavaPoet Type builder for an AbstractFlowrDeepLinkHandler implementation class.
     *
     * @param className the name to be used for the implementation class appended
     *                           by {@link #HANDLER_FILE_NAME_POST_FIX}
     * @return The builder for the implementation class.
     */
    private static TypeSpec.Builder getClassObject(String className) {
        return TypeSpec.classBuilder(className + HANDLER_FILE_NAME_POST_FIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(ClassName.get(FLOWR_INTERNAL_PACKAGE_NAME, ABSTRACT_HANDLER_CLASS_NAME));
    }

    /**
     * Create the constructor for deep link handler implementation class.
     *
     * @return The Constructor JavaPoet object.
     */
    private static MethodSpec.Builder generateConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
    }

    /**
     * Generate a new deep link handler implementation class with the specified package name,
     * class name and the constructor body.
     *
     * @param packageName        the name of the package to use for the generated class.
     * @param className          the name to be used for the implementation class appended
     *                           by {@link #HANDLER_FILE_NAME_POST_FIX}
     * @param constructorBuilder the constructor body builder for the class.
     */
    private void generateDeepLinkHandler(String packageName, String className,
                                         MethodSpec.Builder constructorBuilder) {
        TypeSpec classObject = getClassObject(className)
                .addMethod(constructorBuilder.build())
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, classObject)
                .build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
