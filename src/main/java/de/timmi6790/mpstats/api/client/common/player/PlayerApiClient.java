package de.timmi6790.mpstats.api.client.common.player;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.AbstractApiClient;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.common.player.deserializers.GeneratedPlayerEntryDeserializer;
import de.timmi6790.mpstats.api.client.common.player.deserializers.PlayerEntryDeserializer;
import de.timmi6790.mpstats.api.client.common.player.deserializers.PlayerStatsDeserializer;
import de.timmi6790.mpstats.api.client.common.player.models.GeneratedPlayerEntry;
import de.timmi6790.mpstats.api.client.common.player.models.Player;
import de.timmi6790.mpstats.api.client.common.player.models.PlayerEntry;
import de.timmi6790.mpstats.api.client.common.player.models.PlayerStats;
import lombok.Getter;
import okhttp3.HttpUrl;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Getter(PROTECTED)
public class PlayerApiClient<P extends Player> extends AbstractApiClient {
    private final Class<?> playerClass;

    public PlayerApiClient(final String baseUrl,
                           final String apiKey,
                           final String schema,
                           final ObjectMapper objectMapper,
                           final Class<P> playerClass) {
        super(baseUrl, apiKey, schema, objectMapper);

        this.playerClass = playerClass;

        final JavaType playerStatsType = this.getPlayerStatsType();
        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(PlayerStats.class, new PlayerStatsDeserializer<>(playerStatsType, playerClass))
                        .addDeserializer(GeneratedPlayerEntry.class, new GeneratedPlayerEntryDeserializer(GeneratedPlayerEntry.class))
                        .addDeserializer(PlayerEntry.class, new PlayerEntryDeserializer(PlayerEntry.class))
        );
    }

    protected String getPlayerBaseUrl() {
        return this.getBaseSchemaUrl() + "/player";
    }

    protected final JavaType getPlayerStatsType() {
        return this.getObjectMapper().getTypeFactory().constructParametricType(PlayerStats.class, this.playerClass);
    }

    public Optional<PlayerStats<P>> getPlayerGameStats(final String playerName,
                                                       final String gameName,
                                                       final String boardName,
                                                       final Set<Reason> filterReasons) {
        return this.getPlayerGameStats(
                playerName,
                gameName,
                boardName,
                ZonedDateTime.now(),
                filterReasons
        );
    }

    public Optional<PlayerStats<P>> getPlayerGameStats(final String playerName,
                                                       final String gameName,
                                                       final String boardName,
                                                       final ZonedDateTime saveTime,
                                                       final Set<Reason> filterReasons) {
        final HttpUrl.Builder httpBuilder = HttpUrl.parse(this.getPlayerBaseUrl() + "/" + playerName + "/stats/game/" + gameName + "/" + boardName)
                .newBuilder()
                .addQueryParameter("saveTime", saveTime.toString());
        for (final Reason reason : filterReasons) {
            httpBuilder.addQueryParameter("filterReasons", reason.toString());
        }
        return this.getGetResponse(
                httpBuilder.build(),
                this.getPlayerStatsType()
        );
    }

    public Optional<PlayerStats<P>> getPlayerStatStats(final String playerName,
                                                       final String statName,
                                                       final String boardName,
                                                       final Set<Reason> filterReasons) {
        return this.getPlayerStatStats(
                playerName,
                statName,
                boardName,
                ZonedDateTime.now(),
                filterReasons
        );
    }

    public Optional<PlayerStats<P>> getPlayerStatStats(final String playerName,
                                                       final String statName,
                                                       final String boardName,
                                                       final ZonedDateTime saveTime,
                                                       final Set<Reason> filterReasons) {
        final HttpUrl.Builder httpBuilder = HttpUrl.parse(this.getPlayerBaseUrl() + "/" + playerName + "/stats/stat/" + statName + "/" + boardName)
                .newBuilder()
                .addQueryParameter("saveTime", saveTime.toString());
        for (final Reason reason : filterReasons) {
            httpBuilder.addQueryParameter("filterReasons", reason.toString());
        }
        return this.getGetResponse(
                httpBuilder.build(),
                this.getPlayerStatsType()
        );
    }
}
