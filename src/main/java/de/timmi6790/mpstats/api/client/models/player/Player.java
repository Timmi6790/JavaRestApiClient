package de.timmi6790.mpstats.api.client.models.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Player {
    private final String name;
}
