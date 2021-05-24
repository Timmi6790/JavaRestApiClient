package de.timmi6790.mpstats.api.client.common.player.models;

import lombok.Data;

import java.util.Set;

@Data
public class PlayerStats<P extends Player> {
    private final P player;
    private final Set<GeneratedPlayerEntry> generatedStats;
    private final Set<PlayerEntry> stats;
}
