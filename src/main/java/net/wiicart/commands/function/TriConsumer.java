package net.wiicart.commands.function;

import java.util.function.Function;

/**
 * A {@link Function} with three variables.
 * @param <T> The first variable type
 * @param <U> The second variable type
 * @param <V> The third variable type
 * @param <R> The return type
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    /**
     * Applies this function with the given arguments
     * @param t The first argument
     * @param u The second argument
     * @param v The third argument
     * @return The result
     */
    R apply(T t, U u, V v);

}
