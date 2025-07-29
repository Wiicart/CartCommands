package net.wiicart.commands.function;

import java.util.function.Function;

/**
 * A {@link Function} with four arguments.
 * @param <T> The first argument type
 * @param <U> The second argument type
 * @param <V> The third argument type
 * @param <W> The fourth argument type
 * @param <R> The return type
 */
@FunctionalInterface
public interface QuadFunction<T, U, V, W, R> {

    R apply(T t, U u, V v, W w);

}
