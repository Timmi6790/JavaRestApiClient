package de.timmi6790.mpstats.api.client.common.group.exceptions;

import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.time.ZonedDateTime;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
public class InvalidGroupNameRestException extends BaseRestException {
    @Serial
    private static final long serialVersionUID = 2171140119304831592L;

    public InvalidGroupNameRestException(final ZonedDateTime timestamp,
                                         final int status,
                                         final String error,
                                         final String path,
                                         final String message) {
        super(timestamp, status, error, path, message);
    }
}
