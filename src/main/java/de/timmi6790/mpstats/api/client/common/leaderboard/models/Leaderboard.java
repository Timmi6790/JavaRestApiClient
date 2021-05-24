package de.timmi6790.mpstats.api.client.common.leaderboard.models;


import de.timmi6790.mpstats.api.client.common.board.models.Board;
import de.timmi6790.mpstats.api.client.common.game.models.Game;
import de.timmi6790.mpstats.api.client.common.stat.models.Stat;
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
