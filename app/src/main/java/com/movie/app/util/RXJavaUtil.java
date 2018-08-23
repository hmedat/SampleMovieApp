package com.movie.app.util;

import android.util.Pair;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class RXJavaUtil {
    private static final int UNCHECKED_ERROR_TYPE_CODE = -100;


    @SafeVarargs
    public static Function<? super Observable<Throwable>, ? extends ObservableSource<?>>
    exponentialBackoffForExceptions(final long initialDelay, final int numRetries
            , final TimeUnit unit, final Class<? extends Throwable>... errorTypes) {

        if (initialDelay <= 0) {
            throw new IllegalArgumentException("initialDelay must be greater than 0");
        }

        if (numRetries <= 0) {
            throw new IllegalArgumentException("numRetries must be greater than 0");
        }

        return errors -> errors
                .zipWith(Observable.range(1, numRetries + 1), (error, integer) -> {
                    if (integer == numRetries + 1) {
                        return new Pair<>(error, UNCHECKED_ERROR_TYPE_CODE);
                    }

                    if (errorTypes != null) {
                        for (Class<? extends Throwable> clazz : errorTypes) {
                            if (clazz.isInstance(error)) {
                                // Mark as error type found
                                return new Pair<>(error, integer);
                            }
                        }
                    }

                    return new Pair<>(error, UNCHECKED_ERROR_TYPE_CODE);
                })
                .flatMap(errorRetryCountTuple -> {
                    int retryAttempt = errorRetryCountTuple.second;
                    // If not a known error type, pass the error through.
                    if (retryAttempt == UNCHECKED_ERROR_TYPE_CODE) {
                        return Observable.error(errorRetryCountTuple.first);
                    }
                    long delay = (long) Math.pow(initialDelay, retryAttempt);
                    // Else, exponential backoff for the passed in error types.
                    return Observable.timer(delay, unit);
                });
    }
}
