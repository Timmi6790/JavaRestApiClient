package de.timmi6790.mpstats.api.client.java;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.common.BaseApiClient;
import de.timmi6790.mpstats.api.client.java.player.JavaPlayerApiClient;
import de.timmi6790.mpstats.api.client.java.player.deserializers.JavaPlayerDeserializer;
import de.timmi6790.mpstats.api.client.java.player.models.JavaPlayer;
import lombok.Getter;

public class JavaMpStatsApiClient extends BaseApiClient<JavaPlayer> {
    @Getter
    private final JavaPlayerApiClient playerClient;

    public JavaMpStatsApiClient(final String baseUrl, final String apiKey) {
        super(baseUrl, apiKey, "java", JavaPlayer.class);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(JavaPlayer.class, new JavaPlayerDeserializer(JavaPlayer.class))
        );

        this.playerClient = new JavaPlayerApiClient(baseUrl, apiKey, this.getSchema(), this.getObjectMapper(), this.getExceptionHandler());
    }
}
