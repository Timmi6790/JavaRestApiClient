package de.timmi6790.mpstats.api.client.common.player.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class PlayerStats<P extends Player> {
    private final P player;
    private final Set<GeneratedPlayerEntry> generatedStats;
    private final Set<PlayerEntry> stats;

    public PlayerStats(final PlayerStats<P> playerStats) {
        this(playerStats.getPlayer(), playerStats.getGeneratedStats(), playerStats.getStats());
    }
}
