package de.timmi6790.mpstats.api.client.common.game.exceptions;

import de.timmi6790.mpstats.api.client.common.game.models.GameCategory;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
public class InvalidGameCategoryNameRestException extends BaseRestException {
    @Serial
    private static final long serialVersionUID = -6844535176597348955L;
    private final List<GameCategory> suggestedGameCategories;

    public InvalidGameCategoryNameRestException(final BaseRestException baseRestException,
                                                final List<GameCategory> suggestedGameCategories) {
        super(baseRestException);

        this.suggestedGameCategories = suggestedGameCategories;
    }
}
