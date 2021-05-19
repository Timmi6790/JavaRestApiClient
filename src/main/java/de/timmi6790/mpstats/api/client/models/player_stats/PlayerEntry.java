package de.timmi6790.mpstats.api.client.models.player_stats;

import de.timmi6790.mpstats.api.client.models.leaderboard.Leaderboard;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class PlayerEntry {
    private final Leaderboard leaderboard;
    private final ZonedDateTime saveTime;
    private final long score;
    private final int position;
}
