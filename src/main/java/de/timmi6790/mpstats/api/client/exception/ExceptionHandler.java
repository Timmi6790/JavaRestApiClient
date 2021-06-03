package de.timmi6790.mpstats.api.client.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.exception.deserializers.BaseRestExceptionDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExceptionHandler {
    private final Map<String, Class<? extends BaseRestException>> exceptions = new HashMap<>();

    public ExceptionHandler(final ObjectMapper objectMapper) {
        objectMapper.registerModule(
                new SimpleModule()
                        .addDeserializer(BaseRestException.class, new BaseRestExceptionDeserializer(BaseRestException.class))

        );
    }

    public void registerException(final String errorCode, final Class<? extends BaseRestException> exceptionClass) {
        this.exceptions.put(errorCode, exceptionClass);
    }

    public Optional<Class<? extends BaseRestException>> getRestExceptionClass(final String errorCode) {
        return Optional.ofNullable(this.exceptions.get(errorCode));
    }
}
