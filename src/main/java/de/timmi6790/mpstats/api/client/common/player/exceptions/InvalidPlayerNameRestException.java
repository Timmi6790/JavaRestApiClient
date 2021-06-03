package de.timmi6790.mpstats.api.client.common.player.exceptions;

import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InvalidPlayerNameRestException extends BaseRestException {
    @Serial
    private static final long serialVersionUID = -2681393442755850757L;

    public InvalidPlayerNameRestException(final BaseRestException baseRestException) {
        super(baseRestException);
    }
}
