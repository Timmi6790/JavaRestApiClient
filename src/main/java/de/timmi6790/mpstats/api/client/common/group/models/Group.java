package de.timmi6790.mpstats.api.client.common.group.models;

import de.timmi6790.mpstats.api.client.common.game.models.Game;
import lombok.Data;

import java.util.List;

@Data
public class Group {
    private final String groupName;
    private final String cleanName;
    private final String description;
    private final List<String> aliasNames;
    private final List<Game> games;
}
