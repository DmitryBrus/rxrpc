/**
 *
 */
package com.slimgears.rxrpc.apt.java;

import com.google.auto.service.AutoService;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.util.ElementUtils;
import com.slimgears.apt.util.ImportTracker;
import com.slimgears.apt.util.JavaUtils;
import com.slimgears.apt.util.TemplateEvaluator;
import com.slimgears.rxrpc.apt.EndpointGenerator;

import javax.annotation.processing.SupportedOptions;

@AutoService(EndpointGenerator.class)
@SupportedOptions({
        JavaEndpointGenerator.generateClientOption,
        JavaEndpointGenerator.generateServerOption,
        JavaEndpointGenerator.useAutoServiceOption
})
public class JavaEndpointGenerator implements EndpointGenerator {
    static final String generateClientOption = "rxrpc.java.client";
    static final String generateServerOption = "rxrpc.java.server";
    static final String useAutoServiceOption = "rxrpc.java.autoservice";

    @Override
    public void generate(Context context) {
        if (context.hasOption(generateClientOption)) {
            generateClass(context, "_RxClient", "java-client.java.vm");
        }
        if (context.hasOption(generateServerOption)) {
            generateClass(context, "_RxModule", "java-server.java.vm");
        }
    }

    private void generateClass(Context context, String classNameSuffix, String templatePath) {
        String className = context.sourceTypeElement().getQualifiedName() + classNameSuffix;
        context.log().info("Generating class: {}", className);

        TypeInfo targetClass = TypeInfo.of(className);
        ImportTracker importTracker = ImportTracker.create(TypeInfo.packageName(className));

        TemplateEvaluator
                .forResource(templatePath)
                .variables(context)
                .variable("hasModuleName", context.moduleName() != null)
                .variable("isInterface", ElementUtils.isInterface(context.sourceTypeElement()))
                .variable("javaUtils", new JavaUtils())
                .variable("autoService", context.hasOption(useAutoServiceOption))
                .variable("targetClass", targetClass)
                .apply(JavaUtils.imports(importTracker))
                //.postProcess(JavaUtils.formatter())
                .write(JavaUtils.fileWriter(context.environment(), targetClass));
    }
}
