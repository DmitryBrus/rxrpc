/**
 *
 */
package com.slimgears.rxrpc.apt.util;

import com.slimgears.rxrpc.apt.data.TypeInfo;
import com.slimgears.rxrpc.apt.data.TypeParameterInfo;
import org.apache.commons.text.StringSubstitutor;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.function.Function;

public class ImportTracker {
    private final static String importsMagicWord = "`imports`";
    private final Collection<String> imports = new TreeSet<>();
    private final Collection<TypeInfo> usedClasses = new TreeSet<>(TypeInfo.comparator);
    private final String selfPackageName;

    public static ImportTracker create(String selfPackageName) {
        return new ImportTracker(selfPackageName);
    }

    private ImportTracker(String selfPackageName) {
        this.selfPackageName = selfPackageName;
    }

    public String[] imports() {
        return this.imports.toArray(new String[imports.size()]);
    }

    public TypeInfo[] usedClasses() {
        return this.usedClasses.toArray(new TypeInfo[usedClasses.size()]);
    }

    public String use(TypeInfo typeInfo) {
        return simplify(typeInfo).fullName();
    }

    public String use(String cls) {
        TypeInfo typeInfo = TypeInfoParser.parse(cls);
        return use(typeInfo);
    }

    private TypeInfo simplify(TypeInfo typeInfo) {
        usedClasses.add(typeInfo.isArray()
                ? typeInfo.elementType()
                : TypeInfo.of(typeInfo.name()));

        String packageName = typeInfo.packageName();
        if (!packageName.isEmpty() && !packageName.equals(selfPackageName)) {
            imports.add(packageName + "." + typeInfo.simpleName());
        }
        TypeInfo.Builder builder = TypeInfo.builder().name(typeInfo.simpleName());
        typeInfo.typeParams().stream().map(TypeParameterInfo::type)
                .map(this::simplify)
                .forEach(builder::typeParams);
        return builder.build();
    }

    @Override
    public String toString() {
        return importsMagicWord;
    }
}
