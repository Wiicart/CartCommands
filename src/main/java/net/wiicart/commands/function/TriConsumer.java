package net.wiicart.commands.function;

import java.util.function.Consumer;

/**
 * A {@link Consumer} with three variables.
 * @param <T> The first variable type
 * @param <U> The second variable type
 * @param <V> The third variable type
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {

    /**
     * Applies this function with the given arguments
     * @param t The first argument
     * @param u The second argument
     * @param v The third argument
     */
    void accept(T t, U u, V v);

}
