/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.data.mapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author lili.he
 * @param <FROM>
 * @param <TO>
 */
public class DataMapper<FROM, TO> implements Function<FROM, TO> {

    private static String firstToLowerCase(String text) {
        return Character.toLowerCase(text.charAt(0)) + text.substring(1);
    }

    private static Map<String, Method> toMethodMap(String prefix, Class clazz) {
        return Arrays.stream(clazz.getMethods())
                .distinct()
                .filter(method -> !method.isSynthetic())
                .filter(method -> method.getName().startsWith(prefix))
                .collect(
                        Collectors.toMap(
                                //remove prfix, get property name as key
                                method -> firstToLowerCase(method.getName().substring(prefix.length())),
                                // method as map value
                                method -> method
                        )
                );
    }

    private final Map<String, Assembler> propertyAssemblerMap;
    private final Class<FROM> fromClass;
    private final Class<TO> toClass;
    private final Map<String, Method> getterMap;
    private final Map<String, Method> setterMap;

    public DataMapper(Class<FROM> fromClass, Class<TO> toClass) {

        this.fromClass = fromClass;
        this.toClass = toClass;

        getterMap = toMethodMap("get", fromClass);
        setterMap = toMethodMap("set", toClass);

        propertyAssemblerMap = new HashMap<>();

        setterMap
                .keySet()
                .forEach(name -> {
                    // if property is in the getter map
                    if (Objects.nonNull(getterMap.get(name))) {
                        // create a value assembler
                        propertyAssemblerMap.put(name,
                                new AssemblerBuilder()
                                        .childMapper(v -> v)
                                        .emptyIfError(null)
                                        .fromValueGetter(getterMap.get(name))
                                        .toValueSetter(setterMap.get(name))
                                        .build()
                        );
                    }
                }
                );

    }

    @Override
    public TO apply(FROM from) {

        try {
            TO product = toClass.newInstance();
            propertyAssemblerMap.values().forEach(assembler -> assembler.assemble(from, product));
            return product;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(DataMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void addChildRuleByName(String childPropertyName, Assembler propertyAssembler) {
        propertyAssemblerMap.put(childPropertyName, propertyAssembler);
    }

    public void addChildRuleByName(String toChildPropertyName, String fromChildPropertyName) {
        propertyAssemblerMap.put(
                toChildPropertyName,
                new AssemblerBuilder()
                        .childMapper(v -> v)
                        .emptyIfError(null)
                        .fromValueGetter(getterMap.get(fromChildPropertyName))
                        .toValueSetter(setterMap.get(toChildPropertyName))
                        .build());
    }

    public void addChildRuleByName(String toChildPropertyName, String fromChildPropertyName, Function mapper) {
        propertyAssemblerMap.put(
                toChildPropertyName,
                new AssemblerBuilder()
                        .childMapper(mapper)
                        .emptyIfError(null)
                        .fromValueGetter(getterMap.get(fromChildPropertyName))
                        .toValueSetter(setterMap.get(toChildPropertyName))
                        .build());
    }

    public void addChildRuleByName(String toChildPropertyName, Function<FROM, ?> getter) {
        propertyAssemblerMap.put(
                toChildPropertyName,
                new AssemblerBuilder()
                        .childMapper(v -> v)
                        .emptyIfError(null)
                        .fromValueGetter(getter)
                        .toValueSetter(setterMap.get(toChildPropertyName))
                        .build());
    }

    public <V> void addChildRuleByMapper(Function<FROM, V> mapper, String toChildPropertyName) {
        propertyAssemblerMap.put(
                toChildPropertyName,
                new AssemblerBuilder()
                        .childMapper(mapper)
                        .emptyIfError(null)
                        .fromValueGetter(from -> from)
                        .toValueSetter(setterMap.get(toChildPropertyName))
                        .build());
    }

}
