package de.timmi6790.mpstats.api.client.common.group.exceptions;

import de.timmi6790.mpstats.api.client.common.group.models.Group;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
public class InvalidGroupNameRestException extends BaseRestException {
    @Serial
    private static final long serialVersionUID = 2171140119304831592L;

    private final List<Group> suggestedGroups;

    public InvalidGroupNameRestException(final BaseRestException baseRestException,
                                         final List<Group> suggestedGroups) {
        super(baseRestException);
        this.suggestedGroups = suggestedGroups;
    }
}
