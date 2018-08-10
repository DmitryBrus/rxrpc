/**
 *
 */
package com.slimgears.rxrpc.apt.internal;

import com.slimgears.rxrpc.apt.data.TypeInfo;
import com.slimgears.rxrpc.apt.util.TemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

public interface CodeGenerator<C extends CodeGenerator.Context> {
    void generate(C context);

    abstract class Context {
        private final Logger log = LoggerFactory.getLogger(getClass());

        public abstract ProcessingEnvironment environment();
        public abstract TypeElement sourceTypeElement();
        public TemplateUtils utils() { return TemplateUtils.instance; }
        public TypeInfo sourceClass() {
            return TypeInfo.of(sourceTypeElement());
        }
        public Logger log() { return log; }

        public interface Builder<C extends Context, B extends Builder<C, B>> {
            B sourceTypeElement(TypeElement value);
            B environment(ProcessingEnvironment env);
            C build();
        }
    }
}
