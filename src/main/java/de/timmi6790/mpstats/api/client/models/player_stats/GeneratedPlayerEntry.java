package de.timmi6790.mpstats.api.client.models.player_stats;

import de.timmi6790.mpstats.api.client.models.board.Board;
import de.timmi6790.mpstats.api.client.models.game.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class GeneratedPlayerEntry {
    private final String cleanStatName;
    private final Game game;
    private final Board board;
    private final Number score;
}
