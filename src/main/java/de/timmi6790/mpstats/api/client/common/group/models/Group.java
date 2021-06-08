package de.timmi6790.mpstats.api.client.common.group.models;

import de.timmi6790.mpstats.api.client.common.game.models.Game;
import lombok.Data;

import java.util.Set;

@Data
public class Group {
    private final String groupName;
    private final String cleanName;
    private final String description;
    private final Set<String> aliasNames;
    private final Set<Game> games;
}
