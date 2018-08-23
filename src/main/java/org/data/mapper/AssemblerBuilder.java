/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.data.mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lili he
 * @param <T>
 * @param <V>
 * @param <FROM>
 * @param <TO>
 */
public class AssemblerBuilder<T, V, FROM, TO> {

    private Function<T, V> mapper;
    private BiConsumer<TO, V> setter;
    private Function<FROM, T> getter;
    private BiConsumer<FROM, V> reprocessor = (FROM from, V v) -> {
        //do nothing for default
    };

    private V empty;

    public AssemblerBuilder() {

    }

    public AssemblerBuilder<T, V, FROM, TO> childMapper(final Function<T, V> value) {
        this.mapper = value;
        return this;
    }

    public AssemblerBuilder<T, V, FROM, TO> toValueSetter(final BiConsumer<TO, V> value) {
        this.setter = value;
        return this;
    }

    public AssemblerBuilder<T, V, FROM, TO> toValueSetter(final Method setterMethod) {
        Objects.requireNonNull(setterMethod);

        this.setter = setter = (to, value) -> {
            try {
                setterMethod.invoke(to, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {

                Logger.getLogger(AssemblerBuilder.class.getName()).log(Level.SEVERE, "error:", ex);
                Logger.getLogger(AssemblerBuilder.class.getName()).log(Level.SEVERE, "method:", setterMethod);
                Logger.getLogger(AssemblerBuilder.class.getName()).log(Level.SEVERE, "value:", value);
            }

        };
        return this;
    }

    public AssemblerBuilder<T, V, FROM, TO> fromValueGetter(final Function<FROM, T> value) {
        this.getter = value;
        return this;
    }

    public AssemblerBuilder<T, V, FROM, TO> fromValueGetter(final Method getterMethod) {
        Objects.requireNonNull(getterMethod);
        this.getter = getter = from -> {
            try {
                return (T) getterMethod.invoke(from);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(AssemblerBuilder.class.getName()).log(Level.SEVERE, null, ex);
                Logger.getLogger(AssemblerBuilder.class.getName()).log(Level.SEVERE, "method:", getterMethod);
            }
            return null;
        };
        return this;
    }

    public AssemblerBuilder<T, V, FROM, TO> reprocessor(final BiConsumer<FROM, V> value) {
        this.reprocessor = value;
        return this;
    }

    public AssemblerBuilder<T, V, FROM, TO> emptyIfError(final V value) {
        this.empty = value;
        return this;
    }

    public Assembler<T, V, FROM, TO> build() {
        Assembler<T, V, FROM, TO> assembler = new Assembler<T, V, FROM, TO>() {

        };
        assembler.mapper = mapper;
        assembler.setter = setter;
        assembler.getter = getter;
        assembler.reprocessor = reprocessor;
        assembler.empty = empty;
        return assembler;
    }

}
