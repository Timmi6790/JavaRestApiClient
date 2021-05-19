package de.timmi6790.mpstats.api.client.java;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.BaseApiClient;
import de.timmi6790.mpstats.api.client.java.deserializer.player.JavaPlayerDeserializer;
import de.timmi6790.mpstats.api.client.java.models.player.JavaPlayer;

public class JavaMpStatsApiClient extends BaseApiClient<JavaPlayer> {
    public JavaMpStatsApiClient(final String baseUrl, final String apiKey) {
        super(baseUrl, apiKey, "java", JavaPlayer.class);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(JavaPlayer.class, new JavaPlayerDeserializer(JavaPlayer.class))
        );
    }
}
