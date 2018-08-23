/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.data.mapper;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lili.he
 */
public class Resolver {

    public static <T> Optional<T> resolve(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        } catch (NullPointerException e) {
            Logger.getLogger(DataMapper.class.getName()).log(Level.SEVERE, null, e);
            return Optional.empty();
        }
    }

    public static <T> Optional<T> resolveAny(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            Logger.getLogger(DataMapper.class.getName()).log(Level.SEVERE, null, e);
            return Optional.empty();
        }
    }
}
