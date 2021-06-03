package de.timmi6790.mpstats.api.client.common.game.exceptions;

import de.timmi6790.mpstats.api.client.common.game.models.Game;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
public class InvalidGameNameRestException extends BaseRestException {
    @Serial
    private static final long serialVersionUID = 4569493947770683128L;
    private final List<Game> suggestedGames;

    public InvalidGameNameRestException(final BaseRestException baseRestException, final List<Game> suggestedGames) {
        super(baseRestException);

        this.suggestedGames = suggestedGames;
    }
}
