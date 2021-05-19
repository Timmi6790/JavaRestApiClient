package de.timmi6790.mpstats.api.client.models.board;

import lombok.Data;

import java.util.Set;

@Data
public class Board {
    private final String boardName;
    private final String cleanName;
    private final int updateTime;
    private final Set<String> aliasNames;
}
