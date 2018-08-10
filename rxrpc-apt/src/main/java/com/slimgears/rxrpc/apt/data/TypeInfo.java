package com.slimgears.rxrpc.apt.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.slimgears.rxrpc.apt.util.TypeInfoParser;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AutoValue
public abstract class TypeInfo implements HasName, HasMethods, HasAnnotations, HasTypeParameters {
    private final static ImmutableMap<TypeInfo, TypeInfo> boxableTypes = ImmutableMap.<TypeInfo, TypeInfo>builder()
            .put(TypeInfo.of(boolean.class), TypeInfo.of(Boolean.class))
            .put(TypeInfo.of(short.class), TypeInfo.of(Short.class))
            .put(TypeInfo.of(int.class), TypeInfo.of(Integer.class))
            .put(TypeInfo.of(long.class), TypeInfo.of(Long.class))
            .put(TypeInfo.of(float.class), TypeInfo.of(Float.class))
            .put(TypeInfo.of(double.class), TypeInfo.of(Double.class))
            .put(TypeInfo.of(char.class), TypeInfo.of(Character.class))
            .build();

    public abstract Builder toBuilder();

    public String fullName() {
        return (typeParams().isEmpty())
                ? name()
                : name() + typeParams()
                .stream()
                .map(param -> param.type().fullName())
                .collect(Collectors.joining(", ", "<", ">"));
    }

    public TypeInfo elementType() {
        return typeParams().isEmpty()
                ? isArray() ? TypeInfo.of(name().substring(0, name().indexOf("[]"))) : this
                : typeParams().get(0).type();
    }

    public String simpleName() {
        return name().substring(name().lastIndexOf('.') + 1);
    }

    public String packageName() {
        return packageName(name());
    }

    public boolean isArray() {
        return name().endsWith("[]");
    }

    public boolean is(String name) {
        return name().equals(name);
    }

    public boolean is(Class cls) {
        return name().equals(cls.getName());
    }

    public boolean isOneOf(Class... cls) {
        return Stream.of(cls).anyMatch(this::is);
    }

    public static Builder builder() {
        return new AutoValue_TypeInfo.Builder();
    }

    public static TypeInfo arrayOf(TypeInfo typeInfo) {
        return builder().name(typeInfo.elementType().name() + "[]").build();
    }

    public static TypeInfo of(String name, TypeInfo... params) {
        return builder().name(name).typeParams(params).build();
    }

    public static TypeInfo parse(String fullName) {
        return TypeInfoParser.parse(fullName);
    }

    public static TypeInfo of(TypeMirror typeMirror) {
        return parse(typeMirror.toString());
    }

    public static TypeInfo of(Type type) {
        return of(type.getTypeName());
    }

    public static TypeInfo of(TypeElement typeElement) {
        Builder builder = builder()
                .name(typeElement.getQualifiedName().toString())
                .annotationsFromElement(typeElement)
                .typeParams(typeElement.getTypeParameters());

        typeElement.getEnclosedElements()
                .stream()
                .filter(ExecutableElement.class::isInstance)
                .map(ExecutableElement.class::cast)
                .forEach(builder::method);

        return builder.build();
    }

    @AutoValue.Builder
    public interface Builder extends
            InfoBuilder<TypeInfo>,
            HasName.Builder<Builder>,
            HasMethods.Builder<Builder>,
            HasTypeParameters.Builder<Builder>,
            HasAnnotations.Builder<Builder> {
    }

    public TypeInfo asBoxed() {
        return Optional.ofNullable(boxableTypes.get(this)).orElse(this);
    }

    @Override
    public String toString() {
        return fullName();
    }

    public static String packageName(String qualifiedName) {
        int pos = qualifiedName.lastIndexOf('.');
        return (pos >= 0) ? qualifiedName.substring(0, pos) : "";
    }

    public static String packageName(Name name) {
        return packageName(name.toString());
    }
}
