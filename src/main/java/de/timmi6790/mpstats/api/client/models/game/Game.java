package de.timmi6790.mpstats.api.client.models.game;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Data
public class Game {
    private final String gameName;
    private final String cleanName;
    private final Set<String> aliasNames;
    private final String categoryName;
    @Nullable
    private final String description;
    @Nullable
    private final String wikiUrl;
}
