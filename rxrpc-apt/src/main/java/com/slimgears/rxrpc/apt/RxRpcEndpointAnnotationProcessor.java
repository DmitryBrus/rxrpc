package com.slimgears.rxrpc.apt;

import com.google.auto.service.AutoService;
import com.slimgears.rxrpc.apt.data.MethodInfo;
import com.slimgears.rxrpc.apt.internal.AbstractAnnotationProcessor;
import com.slimgears.rxrpc.core.RxRpcEndpoint;
import com.slimgears.rxrpc.core.RxRpcMethod;

import javax.annotation.Generated;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.slimgears.rxrpc.core.RxRpcEndpoint", "javax.annotation.Generated"})
public class RxRpcEndpointAnnotationProcessor extends AbstractAnnotationProcessor<EndpointGenerator, EndpointGenerator.Context> {
    private final Collection<CodeGenerationFinalizer> finalizers = new ArrayList<>();

    public RxRpcEndpointAnnotationProcessor() {
        super(EndpointGenerator.class);
        ServiceLoader.load(CodeGenerationFinalizer.class, getClass().getClassLoader()).forEach(finalizers::add);
    }

    public RxRpcEndpointAnnotationProcessor(EndpointGenerator... generators) {
        super(generators);
    }

    @Override
    protected boolean processAnnotation(TypeElement annotationType, RoundEnvironment roundEnv) {
        if (!annotationType.getQualifiedName().toString().equals(Generated.class.getName())) {
            return super.processAnnotation(annotationType, roundEnv);
        }

        if (roundEnv.getElementsAnnotatedWith(annotationType)
                .stream()
                .map(element -> element.getAnnotation(Generated.class))
                .flatMap(g -> Stream.of(g.value()))
                .anyMatch(g -> g.equals(getClass().getName()))) {
            onFinalize(annotationType);
        }

        return false;
    }

    private void onFinalize(TypeElement annotationType) {
        CodeGenerationFinalizer.Context context = CodeGenerationFinalizer.Context.builder()
                .processorClass(getClass())
                .sourceTypeElement(annotationType)
                .environment(processingEnv)
                .build();
        finalizers.forEach(f -> f.generate(context));
        finalizers.clear();
    }

    @Override
    protected EndpointGenerator.Context createContext(TypeElement annotationType, TypeElement typeElement) {
        Collection<MethodInfo> methods = typeElement
                .getEnclosedElements()
                .stream()
                .filter(el -> el.getAnnotation(RxRpcMethod.class) != null)
                .filter(ExecutableElement.class::isInstance)
                .map(ExecutableElement.class::cast)
                .map(MethodInfo::of)
                .collect(Collectors.toList());

        return EndpointGenerator.Context.builder()
                .processorClass(getClass())
                .sourceTypeElement(typeElement)
                .environment(processingEnv)
                .meta(typeElement.getAnnotation(RxRpcEndpoint.class))
                .addMethods(methods)
                .build();
    }
}
