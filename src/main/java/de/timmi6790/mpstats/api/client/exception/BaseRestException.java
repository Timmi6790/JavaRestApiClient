package de.timmi6790.mpstats.api.client.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.ZonedDateTime;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class BaseRestException extends Exception {
    @Serial
    private static final long serialVersionUID = 173635360388717305L;

    public BaseRestException(final BaseRestException baseRestException) {
        this(
                baseRestException.getTimestamp(),
                baseRestException.getStatus(),
                baseRestException.getError(),
                baseRestException.getPath(),
                baseRestException.getMessage()
        );
    }

    private final ZonedDateTime timestamp;
    private final int status;
    private final String error;
    private final String path;
    private final String message;
}
