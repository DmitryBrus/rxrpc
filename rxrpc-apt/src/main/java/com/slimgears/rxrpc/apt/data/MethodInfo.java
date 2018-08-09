package com.slimgears.rxrpc.apt.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.lang.model.element.ExecutableElement;

@AutoValue
public abstract class MethodInfo implements HasName, HasAnnotations, HasTypeParameters {
    public abstract ImmutableList<ParamInfo> params();
    public abstract TypeInfo returnType();

    public static Builder builder() {
        return new AutoValue_MethodInfo.Builder();
    }

    public static MethodInfo of(ExecutableElement executableElement) {
        Builder builder = builder()
                .name(executableElement.getSimpleName().toString())
                .typeParams(executableElement.getTypeParameters())
                .returnType(TypeInfo.of(executableElement.getReturnType()));

        executableElement.getParameters()
                .stream()
                .map(ParamInfo::of)
                .forEach(builder::addParam);

        return builder.build();
    }

    @AutoValue.Builder
    public interface Builder extends
            InfoBuilder<MethodInfo>,
            HasName.Builder<Builder>,
            HasAnnotations.Builder<Builder>,
            HasTypeParameters.Builder<Builder> {
        ImmutableList.Builder<ParamInfo> paramsBuilder();
        Builder returnType(TypeInfo type);

        default Builder addParam(ParamInfo param) {
            paramsBuilder().add(param);
            return this;
        }

        default Builder addParam(String name, TypeInfo type) {
            return addParam(ParamInfo.create(name, type));
        }
    }
}
