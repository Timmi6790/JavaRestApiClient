package de.timmi6790.mpstats.api.client.bedrock.player.models;

import de.timmi6790.mpstats.api.client.common.player.models.Player;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
public class BedrockPlayer extends Player {
    public BedrockPlayer(final String name) {
        super(name);
    }
}
