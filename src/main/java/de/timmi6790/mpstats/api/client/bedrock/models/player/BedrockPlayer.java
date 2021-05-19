package de.timmi6790.mpstats.api.client.bedrock.models.player;

import de.timmi6790.mpstats.api.client.models.player.Player;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
public class BedrockPlayer extends Player {
    public BedrockPlayer(final String name) {
        super(name);
    }
}
