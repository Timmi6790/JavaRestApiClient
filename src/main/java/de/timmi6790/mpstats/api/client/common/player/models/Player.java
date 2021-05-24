package de.timmi6790.mpstats.api.client.common.player.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Player {
    private final String name;
}
