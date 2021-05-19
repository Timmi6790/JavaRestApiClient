package de.timmi6790.mpstats.api.client.models.leaderboard;


import de.timmi6790.mpstats.api.client.models.board.Board;
import de.timmi6790.mpstats.api.client.models.game.Game;
import de.timmi6790.mpstats.api.client.models.stat.Stat;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Leaderboard {
    private final Game game;
    private final Stat stat;
    private final Board board;
    private final boolean deprecated;
    private final ZonedDateTime lastSaveTime;
    private final ZonedDateTime lastCacheSaveTime;
}
