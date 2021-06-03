package de.timmi6790.mpstats.api.client.common.leaderboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.AbstractApiClient;
import de.timmi6790.mpstats.api.client.common.board.exceptions.InvalidBoardNameException;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.common.game.exceptions.InvalidGameNameRestException;
import de.timmi6790.mpstats.api.client.common.leaderboard.deserializers.*;
import de.timmi6790.mpstats.api.client.common.leaderboard.exceptions.InvalidLeaderboardCombinationRestException;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.Leaderboard;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.LeaderboardEntry;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.LeaderboardPositionEntry;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.LeaderboardPositionSave;
import de.timmi6790.mpstats.api.client.common.player.models.Player;
import de.timmi6790.mpstats.api.client.common.stat.exceptions.InvalidStatNameRestException;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import de.timmi6790.mpstats.api.client.exception.ExceptionHandler;
import de.timmi6790.mpstats.api.client.exception.exceptions.UnknownApiException;
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
                                final ExceptionHandler exceptionHandler,
                                final Class<P> playerClass) {
        super(baseUrl, apiKey, schema, objectMapper, exceptionHandler);

        final JavaType leaderboardPositionSaveType = this.getObjectMapper().getTypeFactory().constructParametricType(LeaderboardPositionSave.class, playerClass);
        final JavaType leaderboardEntryType = this.getObjectMapper().getTypeFactory().constructParametricType(LeaderboardEntry.class, playerClass);
        final JavaType leaderboardPositionEntryType = this.getObjectMapper().getTypeFactory().constructParametricType(LeaderboardPositionEntry.class, playerClass);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(Leaderboard.class, new LeaderboardDeserializer(Leaderboard.class))
                        .addDeserializer(LeaderboardEntry.class, new LeaderboardEntryDeserializer<>(leaderboardEntryType, playerClass))
                        .addDeserializer(LeaderboardPositionEntry.class, new LeaderboardPositionEntryDeserializer<>(leaderboardPositionEntryType, leaderboardEntryType))
                        .addDeserializer(LeaderboardPositionSave.class, new LeaderboardPositionSaveDeserializer<>(leaderboardPositionSaveType, leaderboardPositionEntryType))
                        .addDeserializer(InvalidLeaderboardCombinationRestException.class, new InvalidLeaderboardCombinationRestExceptionDeserializer(InvalidLeaderboardCombinationRestException.class))
        );

        exceptionHandler.registerException("leaderboard-1", InvalidLeaderboardCombinationRestException.class);
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

    public Leaderboard getLeaderboard(final String gameName,
                                      final String statName,
                                      final String boardName) throws InvalidGameNameRestException, InvalidStatNameRestException, InvalidLeaderboardCombinationRestException, InvalidBoardNameException {
        final HttpUrl url = HttpUrl.parse(this.getLeaderboardBaseUrl() + "/" + gameName + "/" + statName + "/" + boardName);
        try {
            return this.getGetResponseThrow(
                    url,
                    Leaderboard.class
            );
        } catch (final InvalidGameNameRestException | InvalidStatNameRestException | InvalidBoardNameException | InvalidLeaderboardCombinationRestException e) {
            throw e;
        } catch (final BaseRestException baseRestException) {
            throw new UnknownApiException(baseRestException);
        }
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
                                                       final String boardName) throws InvalidGameNameRestException, InvalidStatNameRestException, InvalidLeaderboardCombinationRestException, InvalidBoardNameException {
        final HttpUrl url = HttpUrl.parse(this.getLeaderboardBaseUrl() + "/" + gameName + "/" + statName + "/" + boardName + "/saves");
        try {
            return this.getGetResponseThrow(
                    url,
                    new TypeReference<>() {
                    }
            );
        } catch (final InvalidGameNameRestException | InvalidStatNameRestException | InvalidBoardNameException | InvalidLeaderboardCombinationRestException e) {
            throw e;
        } catch (final BaseRestException baseRestException) {
            throw new UnknownApiException(baseRestException);
        }
    }

    public Optional<LeaderboardPositionSave<P>> getLeaderboardSave(final String gameName,
                                                                   final String statName,
                                                                   final String boardName,
                                                                   final Set<Reason> filterReasons) throws InvalidGameNameRestException, InvalidStatNameRestException, InvalidLeaderboardCombinationRestException, InvalidBoardNameException {
        return this.getLeaderboardSave(gameName, statName, boardName, ZonedDateTime.now(), filterReasons);
    }

    public Optional<LeaderboardPositionSave<P>> getLeaderboardSave(final String gameName,
                                                                   final String statName,
                                                                   final String boardName,
                                                                   final ZonedDateTime saveTime,
                                                                   final Set<Reason> filterReasons) throws InvalidGameNameRestException, InvalidStatNameRestException, InvalidLeaderboardCombinationRestException, InvalidBoardNameException {
        final HttpUrl.Builder httpBuilder = HttpUrl.parse(this.getLeaderboardBaseUrl() + "/" + gameName + "/" + statName + "/" + boardName + "/save")
                .newBuilder()
                .addQueryParameter("saveTime", saveTime.toString());
        for (final Reason reason : filterReasons) {
            httpBuilder.addQueryParameter("filterReasons", reason.toString());
        }
        try {
            return this.getGetResponseThrow(
                    httpBuilder.build(),
                    new TypeReference<>() {
                    }
            );
        } catch (final InvalidGameNameRestException | InvalidStatNameRestException | InvalidBoardNameException | InvalidLeaderboardCombinationRestException e) {
            throw e;
        } catch (final BaseRestException baseRestException) {
            throw new UnknownApiException(baseRestException);
        }
    }
}
