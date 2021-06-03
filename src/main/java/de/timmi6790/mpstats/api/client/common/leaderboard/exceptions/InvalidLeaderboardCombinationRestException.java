package de.timmi6790.mpstats.api.client.common.leaderboard.exceptions;

import de.timmi6790.mpstats.api.client.common.leaderboard.models.Leaderboard;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
public class InvalidLeaderboardCombinationRestException extends BaseRestException {
    @Serial
    private static final long serialVersionUID = -8467123826537737011L;
    private final List<Leaderboard> suggestedLeaderboards;

    public InvalidLeaderboardCombinationRestException(final BaseRestException baseRestException,
                                                      final List<Leaderboard> suggestedLeaderboards) {
        super(baseRestException);

        this.suggestedLeaderboards = suggestedLeaderboards;
    }
}
