package de.timmi6790.mpstats.api.client.exceptions;

import java.io.Serial;

public class ApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4380217037550663958L;

    public ApiException() {
        super("Replace with real text.");
    }
}
