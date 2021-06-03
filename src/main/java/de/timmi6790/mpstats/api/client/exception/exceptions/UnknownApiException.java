package de.timmi6790.mpstats.api.client.exception.exceptions;

import lombok.NoArgsConstructor;

import java.io.Serial;

@NoArgsConstructor
public class UnknownApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4137027973879941500L;

    public UnknownApiException(final String message) {
        super(message);
    }

    public UnknownApiException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnknownApiException(final Throwable cause) {
        super(cause);
    }
}
