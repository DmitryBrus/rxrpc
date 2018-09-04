/**
 *
 */
package com.slimgears.rxrpc.apt;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.slimgears.apt.data.MethodInfo;
import com.slimgears.rxrpc.apt.internal.CodeGenerator;

import javax.annotation.Nullable;

public interface EndpointGenerator extends CodeGenerator<EndpointGenerator.Context> {
    @AutoValue
    abstract class Context extends CodeGenerator.Context {
        public abstract String endpointName();
        public abstract ImmutableList<MethodInfo> methods();
        @Nullable public abstract String moduleName();
        public abstract Builder toBuilder();

        public static Builder builder() {
            return new AutoValue_EndpointGenerator_Context.Builder();
        }

        @AutoValue.Builder
        public interface Builder extends CodeGenerator.Context.Builder<Context, Builder> {
            Builder endpointName(String value);
            Builder moduleName(String value);
            ImmutableList.Builder<MethodInfo> methodsBuilder();

            default Builder addMethod(MethodInfo method) {
                methodsBuilder().add(method);
                return this;
            }

            default Builder addMethods(Iterable<MethodInfo> methods) {
                methodsBuilder().addAll(methods);
                return this;
            }
        }
    }
}
