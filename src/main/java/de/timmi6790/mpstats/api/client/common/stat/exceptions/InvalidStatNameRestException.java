package de.timmi6790.mpstats.api.client.common.stat.exceptions;

import de.timmi6790.mpstats.api.client.common.stat.models.Stat;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
public class InvalidStatNameRestException extends BaseRestException {
    @Serial
    private static final long serialVersionUID = 239961381256976044L;
    private final List<Stat> suggestedStats;

    public InvalidStatNameRestException(final BaseRestException baseRestException,
                                        final List<Stat> suggestedStats) {
        super(baseRestException);

        this.suggestedStats = suggestedStats;
    }
}
