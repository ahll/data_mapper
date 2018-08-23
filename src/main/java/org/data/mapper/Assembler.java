/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.data.mapper;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import static org.data.mapper.Resolver.resolve;

/**
 *
 * @author lili.he
 * @param <T>
 * @param <V>
 * @param <FROM>
 * @param <TO>
 */
public abstract class Assembler<T, V, FROM, TO> {

    protected Function<T, V> mapper;
    protected BiConsumer<TO, V> setter;
    protected Function<FROM, T> getter;
    protected BiConsumer<FROM, V> reprocessor = (FROM, V) -> {
        //do nothing for default
    };
    protected V empty;

    /**
     * Get value from material, transform value, and set to product.
     *
     * @param material
     * @param product
     */
    public void assemble(FROM material, TO product) {

        /**
         * Extract material.
         */
        Optional<T> materialExtracted = resolve(() -> getter.apply(material));
        /**
         * Transform material to assembly and set default empty value.
         */
        V assembly = materialExtracted.map(mapper).orElse(empty);

        /**
         * Reprocess with material.
         */
        reprocessor.accept(material, assembly);

        /**
         * assemble assembly to product.
         */
        setter.accept(product, assembly);
    }

}
