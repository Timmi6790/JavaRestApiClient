package de.timmi6790.mpstats.api.client.common.filter.models;

import de.timmi6790.mpstats.api.client.common.leaderboard.models.Leaderboard;
import de.timmi6790.mpstats.api.client.common.player.models.Player;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Filter<P extends Player> {
    private final P player;
    private final Leaderboard leaderboard;
    private final Reason reason;
    private final ZonedDateTime start;
    private final ZonedDateTime end;
}
