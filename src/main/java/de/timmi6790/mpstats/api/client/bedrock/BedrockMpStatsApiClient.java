package de.timmi6790.mpstats.api.client.bedrock;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.BaseApiClient;
import de.timmi6790.mpstats.api.client.bedrock.deserializer.player.BedrockPlayerDeserializer;
import de.timmi6790.mpstats.api.client.bedrock.models.player.BedrockPlayer;

public class BedrockMpStatsApiClient extends BaseApiClient<BedrockPlayer> {
    public BedrockMpStatsApiClient(final String baseUrl, final String apiKey) {
        super(baseUrl, apiKey, "bedrock", BedrockPlayer.class);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(BedrockPlayer.class, new BedrockPlayerDeserializer(BedrockPlayer.class))
        );
    }
}
