package de.timmi6790.mpstats.api.client.common.leaderboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.AbstractApiClient;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.common.leaderboard.deserializers.LeaderboardDeserializer;
import de.timmi6790.mpstats.api.client.common.leaderboard.deserializers.LeaderboardEntryDeserializer;
import de.timmi6790.mpstats.api.client.common.leaderboard.deserializers.LeaderboardPositionEntryDeserializer;
import de.timmi6790.mpstats.api.client.common.leaderboard.deserializers.LeaderboardPositionSaveDeserializer;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.Leaderboard;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.LeaderboardEntry;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.LeaderboardPositionEntry;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.LeaderboardPositionSave;
import de.timmi6790.mpstats.api.client.common.player.models.Player;
import okhttp3.HttpUrl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LeaderboardApiClient<P extends Player> extends AbstractApiClient {
    public LeaderboardApiClient(final String baseUrl,
                                final String apiKey,
                                final String schema,
                                final ObjectMapper objectMapper,
                                final Class<P> playerClass) {
        super(baseUrl, apiKey, schema, objectMapper);

        final JavaType leaderboardPositionSaveType = this.getObjectMapper().getTypeFactory().constructParametricType(LeaderboardPositionSave.class, playerClass);
        final JavaType leaderboardEntryType = this.getObjectMapper().getTypeFactory().constructParametricType(LeaderboardEntry.class, playerClass);
        final JavaType leaderboardPositionEntryType = this.getObjectMapper().getTypeFactory().constructParametricType(LeaderboardPositionEntry.class, playerClass);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(Leaderboard.class, new LeaderboardDeserializer(Leaderboard.class))
                        .addDeserializer(LeaderboardEntry.class, new LeaderboardEntryDeserializer<>(leaderboardEntryType, playerClass))
                        .addDeserializer(LeaderboardPositionEntry.class, new LeaderboardPositionEntryDeserializer<>(leaderboardPositionEntryType, leaderboardEntryType))
                        .addDeserializer(LeaderboardPositionSave.class, new LeaderboardPositionSaveDeserializer<>(leaderboardPositionSaveType, leaderboardPositionEntryType))
        );
    }

    protected String getLeaderboardBaseUrl() {
        return this.getBaseSchemaUrl() + "/leaderboard";
    }

    public List<Leaderboard> getLeaderboards() {
        final HttpUrl url = HttpUrl.parse(this.getLeaderboardBaseUrl());
        return this.getGetResponse(
                url,
                new TypeReference<List<Leaderboard>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<Leaderboard> getLeaderboard(final String gameName,
                                                final String statName,
                                                final String boardName) {
        final HttpUrl url = HttpUrl.parse(this.getLeaderboardBaseUrl() + "/" + gameName + "/" + statName + "/" + boardName);
        return this.getGetResponse(
                url,
                Leaderboard.class
        );
    }

    public Optional<Leaderboard> createdLeaderboard(final String gameName,
                                                    final String statName,
                                                    final String boardName,
                                                    final boolean deprecated) {
        // "/{gameName}/{statName}/{boardName}"
        throw new UnsupportedOperationException();
    }

    public List<ZonedDateTime> getLeaderboardSaveTimes(final String gameName,
                                                       final String statName,
                                                       final String boardName) {
        final HttpUrl url = HttpUrl.parse(this.getLeaderboardBaseUrl() + "/" + gameName + "/" + statName + "/" + boardName + "/saves");
        return this.getGetResponse(
                url,
                new TypeReference<List<ZonedDateTime>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<LeaderboardPositionSave<P>> getLeaderboardSave(final String gameName,
                                                                   final String statName,
                                                                   final String boardName,
                                                                   final Set<Reason> filterReasons) {
        return this.getLeaderboardSave(gameName, statName, boardName, ZonedDateTime.now(), filterReasons);
    }

    public Optional<LeaderboardPositionSave<P>> getLeaderboardSave(final String gameName,
                                                                   final String statName,
                                                                   final String boardName,
                                                                   final ZonedDateTime saveTime,
                                                                   final Set<Reason> filterReasons) {
        final HttpUrl.Builder httpBuilder = HttpUrl.parse(this.getLeaderboardBaseUrl() + "/" + gameName + "/" + statName + "/" + boardName + "/save")
                .newBuilder()
                .addQueryParameter("saveTime", saveTime.toString());
        for (final Reason reason : filterReasons) {
            httpBuilder.addQueryParameter("filterReasons", reason.toString());
        }
        return this.getGetResponse(
                httpBuilder.build(),
                new TypeReference<>() {
                }
        );
    }
}
