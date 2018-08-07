package com.slimgears.rxrpc.apt.data;

import afu.org.checkerframework.checker.oigj.qual.O;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@AutoValue
public abstract class TypeInfo implements HasName {
    public abstract String name();
    public abstract ImmutableList<TypeInfo> typeParams();

    public String fullName() {
        return (typeParams().isEmpty())
                ? name()
                : name() + typeParams()
                .stream()
                .map(TypeInfo::fullName)
                .collect(Collectors.joining(", ", "<", ">"));
    }

    public TypeInfo elementType() {
        return typeParams().get(0);
    }

    public String simpleName() {
        return name().substring(name().lastIndexOf('.') + 1);
    }

    public String packageName() {
        int lastDotIndex = name().lastIndexOf('.');
        return lastDotIndex >= 0
                ? name().substring(0, lastDotIndex)
                : "";
    }

    public boolean is(String name) {
        return name().equals(name);
    }

    public static Builder builder() {
        return new AutoValue_TypeInfo.Builder();
    }

    public static TypeInfo of(String name, TypeInfo... params) {
        return builder().name(name).typeParams(params).build();
    }

    @AutoValue.Builder
    public interface Builder {
        Builder name(String name);
        ImmutableList.Builder<TypeInfo> typeParamsBuilder();
        TypeInfo build();

        default Builder typeParam(TypeInfo param) {
            typeParamsBuilder().add(param);
            return this;
        }

        default Builder typeParams(TypeInfo... params) {
            Arrays.asList(params).forEach(this::typeParam);
            return this;
        }
    }

    @Override
    public String toString() {
        return fullName();
    }
}
