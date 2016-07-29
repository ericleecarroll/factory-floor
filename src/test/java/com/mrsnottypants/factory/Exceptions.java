package com.mrsnottypants.factory;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by Eric on 7/29/2016.
 */
public class Exceptions {

    /**
     * Returns true if calling consumer on t will result in an exception of type e
     * @param consumer Method we expect to throw an exception
     * @param t Parameter passed to the consumer
     * @param e Class of the exception we expect
     * @param <T> Type of the parameter passed to the consumer
     * @param <E> Type of the exception expected
     * @return true if expected exception is thrown
     */
    public static <T, E extends Throwable> boolean isExpected(final Consumer<T> consumer, final T t, final Class<E> e) {
        boolean expected = false;
        try {
            consumer.accept(t);
        } catch (Throwable ex) {
            expected = (ex.getClass().equals(e));
        }
        return expected;
    }

    /**
     * Returns true if calling bi-consumer on t will result in an exception of type e
     * @param consumer Method we expect to throw an exception
     * @param t 1st parameter passed to the consumer
     * @param u 2nd parameter passed to the consumer
     * @param e Class of the exception we expect
     * @param <T> Type of the 1st parameter passed to the consumer
     * @param <U> Type of the 2nd parameter passed to the consumer
     * @param <E> Type of the exception expected
     * @return true if expected exception is thrown
     */
    public static <T, U, E extends Throwable> boolean isExpected(final BiConsumer<T, U> consumer,
                                                                 final T t, final U u, final Class<E> e) {
        boolean expected = false;
        try {
            consumer.accept(t, u);
        } catch (Throwable ex) {
            expected = (ex.getClass().equals(e));
        }
        return expected;
    }

    // prevent instantiation
    private Exceptions() {}
}
