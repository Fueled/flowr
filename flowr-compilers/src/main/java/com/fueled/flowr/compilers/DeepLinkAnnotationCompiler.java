package com.fueled.flowr.compilers;

import com.fueled.flowr.annotations.DeepLink;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

@SupportedAnnotationTypes("com.fueled.flowr.annotations.DeepLink")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class DeepLinkAnnotationCompiler extends AbstractProcessor {
    private static final String deeplinkFormat = "addFragment(\"%1$s\", (Class<? extends T>) %2$s.class);\n";
    private static final String HANDLER_FILE_NAME = "FlowrDeepLinkHandlerImpl";

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

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("processing...");
        List<String> fragmentPathList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        //browse all the annotations and create the FlowrDeepLinkHandler constructor
        for (Element element : roundEnv.getElementsAnnotatedWith(DeepLink.class)) {
            String objectType = element.asType().toString();
            String[] value = element.getAnnotation(DeepLink.class).value();
            fragmentPathList.add(objectType);
            for (String url : value) {
                sb.append(String.format(deeplinkFormat, url, objectType));
            }

        }
        if (fragmentPathList.size() > 0) {
            try {
                //TODO replace with javapoet no choice
                //open the template file
                FileObject test = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, "", "com/fueled/flowr/compilers/FlowrDeepLinkHandler.tpl");
                BufferedReader br = new BufferedReader(new InputStreamReader(test.openInputStream(), "UTF-8"));
                //create generated class file
                String packageName = generateCanonicalName(fragmentPathList);
                System.out.println(packageName + "." + HANDLER_FILE_NAME);
                JavaFileObject source = processingEnv.getFiler().createSourceFile(packageName + "." + HANDLER_FILE_NAME);
                Writer writer = source.openWriter();

                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    currentLine = currentLine.replaceAll("#REPLACE_HERE#", sb.toString());
                    currentLine = currentLine.replaceAll("#GENERATED_PACKAGE#", packageName);
                    System.out.println(currentLine);
                    writer.append(currentLine);
                }
                writer.flush();
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String generateCanonicalName(List<String> fragmentPathList) {
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
        return packagePath;
    }
}
