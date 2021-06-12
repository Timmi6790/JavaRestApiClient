package de.timmi6790.mpstats.api.client.bedrock;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.bedrock.player.BedrockPlayerApiClient;
import de.timmi6790.mpstats.api.client.bedrock.player.deserializers.BedrockPlayerDeserializer;
import de.timmi6790.mpstats.api.client.bedrock.player.models.BedrockPlayer;
import de.timmi6790.mpstats.api.client.common.BaseApiClient;
import lombok.Getter;

public class BedrockMpStatsApiClient extends BaseApiClient<BedrockPlayer> {
    @Getter
    private final BedrockPlayerApiClient playerClient;

    public BedrockMpStatsApiClient(final String baseUrl, final String apiKey) {
        super(baseUrl, apiKey, "bedrock", BedrockPlayer.class);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(BedrockPlayer.class, new BedrockPlayerDeserializer())
        );

        this.playerClient = new BedrockPlayerApiClient(baseUrl, apiKey, this.getSchema(), this.getObjectMapper(), this.getExceptionHandler());
    }
}
