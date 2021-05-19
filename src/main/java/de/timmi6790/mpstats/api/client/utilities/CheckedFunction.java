package de.timmi6790.mpstats.api.client.utilities;

@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;
}
