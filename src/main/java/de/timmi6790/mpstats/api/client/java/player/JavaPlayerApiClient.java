package de.timmi6790.mpstats.api.client.java.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.timmi6790.mpstats.api.client.common.board.exceptions.InvalidBoardNameException;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.common.game.exceptions.InvalidGameNameRestException;
import de.timmi6790.mpstats.api.client.common.leaderboard.exceptions.InvalidLeaderboardCombinationRestException;
import de.timmi6790.mpstats.api.client.common.player.PlayerApiClient;
import de.timmi6790.mpstats.api.client.common.player.exceptions.InvalidPlayerNameRestException;
import de.timmi6790.mpstats.api.client.common.player.models.PlayerStats;
import de.timmi6790.mpstats.api.client.common.stat.exceptions.InvalidStatNameRestException;
import de.timmi6790.mpstats.api.client.exception.ExceptionHandler;
import de.timmi6790.mpstats.api.client.exception.exceptions.UnknownApiException;
import de.timmi6790.mpstats.api.client.java.player.models.JavaPlayer;
import okhttp3.HttpUrl;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class JavaPlayerApiClient extends PlayerApiClient<JavaPlayer> {
    public JavaPlayerApiClient(final String baseUrl,
                               final String apiKey,
                               final String schema,
                               final ObjectMapper objectMapper,
                               final ExceptionHandler exceptionHandler) {
        super(baseUrl, apiKey, schema, objectMapper, exceptionHandler, JavaPlayer.class);
    }

    public Optional<PlayerStats<JavaPlayer>> getPlayerGameStats(final UUID playerUUID,
                                                                final String gameName,
                                                                final String boardName,
                                                                final boolean includeEmptyEntries,
                                                                final Set<Reason> filterReasons) throws InvalidGameNameRestException, InvalidLeaderboardCombinationRestException, InvalidBoardNameException {
        return this.getPlayerGameStats(
                playerUUID,
                gameName,
                boardName,
                includeEmptyEntries,
                ZonedDateTime.now(),
                filterReasons
        );
    }

    public java.util.Optional<PlayerStats<JavaPlayer>> getPlayerGameStats(final UUID playerUUID,
                                                                          final String gameName,
                                                                          final String boardName,
                                                                          final boolean includeEmptyEntries,
                                                                          final ZonedDateTime saveTime,
                                                                          final Set<Reason> filterReasons) throws InvalidGameNameRestException, InvalidLeaderboardCombinationRestException, InvalidBoardNameException {
        final HttpUrl.Builder httpBuilder = HttpUrl.parse(this.getPlayerBaseUrl() + "/uuid/" + playerUUID.toString() + "/stats/game/" + gameName + "/" + boardName)
                .newBuilder();
        try {
            return this.getPlayerStats(httpBuilder, includeEmptyEntries, saveTime, filterReasons);
        } catch (final InvalidStatNameRestException | InvalidPlayerNameRestException exception) {
            // Should never be thrown
            throw new UnknownApiException(exception);
        }
    }

    public Optional<PlayerStats<JavaPlayer>> getPlayerStatStats(final UUID playerUUID,
                                                                final String statName,
                                                                final String boardName,
                                                                final boolean includeEmptyEntries,
                                                                final Set<Reason> filterReasons) throws InvalidStatNameRestException, InvalidLeaderboardCombinationRestException, InvalidBoardNameException {
        return this.getPlayerStatStats(
                playerUUID,
                statName,
                boardName,
                includeEmptyEntries,
                ZonedDateTime.now(),
                filterReasons
        );
    }

    public Optional<PlayerStats<JavaPlayer>> getPlayerStatStats(final UUID playerUUID,
                                                                final String statName,
                                                                final String boardName,
                                                                final boolean includeEmptyEntries,
                                                                final ZonedDateTime saveTime,
                                                                final Set<Reason> filterReasons) throws InvalidStatNameRestException, InvalidLeaderboardCombinationRestException, InvalidBoardNameException {
        final HttpUrl.Builder httpBuilder = HttpUrl.parse(this.getPlayerBaseUrl() + "/uuid/" + playerUUID.toString() + "/stats/stat/" + statName + "/" + boardName)
                .newBuilder();
        try {
            return this.getPlayerStats(httpBuilder, includeEmptyEntries, saveTime, filterReasons);
        } catch (final InvalidGameNameRestException | InvalidPlayerNameRestException exception) {
            // Should never be thrown
            throw new UnknownApiException(exception);
        }
    }
}
