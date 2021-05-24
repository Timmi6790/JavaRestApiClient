package de.timmi6790.mpstats.api.client.common.player.models;

import de.timmi6790.mpstats.api.client.common.board.models.Board;
import de.timmi6790.mpstats.api.client.common.game.models.Game;
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
