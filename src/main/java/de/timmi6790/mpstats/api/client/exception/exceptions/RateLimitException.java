package de.timmi6790.mpstats.api.client.exception.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Getter
@ToString
public class RateLimitException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8141217877616651925L;

    private final ZonedDateTime retryAt;
}
