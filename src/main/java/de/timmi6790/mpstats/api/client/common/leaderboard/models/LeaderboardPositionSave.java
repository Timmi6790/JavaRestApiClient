package de.timmi6790.mpstats.api.client.common.leaderboard.models;

import de.timmi6790.mpstats.api.client.common.player.models.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class LeaderboardPositionSave<P extends Player> {
    private final Leaderboard leaderboard;
    private final ZonedDateTime saveTime;
    private final List<LeaderboardPositionEntry<P>> entries;
}
