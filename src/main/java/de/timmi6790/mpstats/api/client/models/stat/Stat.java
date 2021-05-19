package de.timmi6790.mpstats.api.client.models.stat;

import lombok.Data;

import java.util.Set;

@Data
public class Stat {
    private final String statName;
    private final String cleanName;
    private final String description;
    private final boolean achievement;
    private final Set<String> aliasNames;
}
