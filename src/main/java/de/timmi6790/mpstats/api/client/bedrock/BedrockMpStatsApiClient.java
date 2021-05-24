package de.timmi6790.mpstats.api.client.bedrock;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.bedrock.player.deserializers.BedrockPlayerDeserializer;
import de.timmi6790.mpstats.api.client.bedrock.player.models.BedrockPlayer;
import de.timmi6790.mpstats.api.client.common.BaseApiClient;

public class BedrockMpStatsApiClient extends BaseApiClient<BedrockPlayer> {
    public BedrockMpStatsApiClient(final String baseUrl, final String apiKey) {
        super(baseUrl, apiKey, "bedrock", BedrockPlayer.class);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(BedrockPlayer.class, new BedrockPlayerDeserializer(BedrockPlayer.class))
        );
    }
}
