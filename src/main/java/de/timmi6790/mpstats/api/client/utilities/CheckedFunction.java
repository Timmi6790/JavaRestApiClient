package de.timmi6790.mpstats.api.client.utilities;

import java.io.IOException;

@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T t) throws IOException;
}
