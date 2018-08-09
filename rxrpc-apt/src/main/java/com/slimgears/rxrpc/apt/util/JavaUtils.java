/**
 *
 */
package com.slimgears.rxrpc.apt.util;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.JavaFormatterOptions;
import com.slimgears.rxrpc.apt.data.TypeInfo;
import org.apache.commons.text.StringSubstitutor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaUtils extends TemplateUtils {
    public static Function<TemplateEvaluator, TemplateEvaluator> imports(ImportTracker importTracker) {
        return evaluator -> evaluator
                .variable("imports", importTracker)
                .postProcess(applyJavaImports(importTracker));
    }

    public static Consumer<String> fileWriter(ProcessingEnvironment environment, TypeInfo targetClass) {
        return code -> writeJavaFile(environment, targetClass, code);
    }

    public static Function<String, String> formatter() {
        return code -> {
            try {
                return new Formatter(JavaFormatterOptions
                        .builder()
                        .style(JavaFormatterOptions.Style.AOSP)
                        .build())
                        .formatSource(code);
            } catch (FormatterException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Function<String, String> applyJavaImports(ImportTracker importTracker) {
        return applyImports(importTracker, imp -> "import " + imp + ";");
    }

    private static Function<String, String> applyImports(ImportTracker importTracker, Function<String, String> importFormatter) {
        StringSubstitutor classSubstitutor = new StringSubstitutor(importTracker::use, "$[", "]", '\\');
        return code -> addImports(importTracker, classSubstitutor.replace(code), importFormatter);
    }

    private static String addImports(ImportTracker importTracker, String code, Function<String, String> importSubstitutor) {
        String importsStr = Stream.of(importTracker.imports())
                .map(importSubstitutor)
                .collect(Collectors.joining("\n"));
        return code.replace(importTracker.toString(), importsStr);
    }

    private static void writeJavaFile(ProcessingEnvironment environment, TypeInfo targetClass, String code) {
        try {
            JavaFileObject sourceFile = environment.getFiler().createSourceFile(targetClass.name());
            try (Writer writer = sourceFile.openWriter()) {
                writer.write(code);
            }
        } catch (IOException e) {
            System.err.println(code);
            throw new RuntimeException(e);
        }
    }
}
