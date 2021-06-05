package de.timmi6790.mpstats.api.client.bedrock.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.timmi6790.mpstats.api.client.bedrock.player.models.BedrockPlayer;
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
import okhttp3.HttpUrl;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

public class BedrockPlayerApiClient extends PlayerApiClient<BedrockPlayer> {
    public BedrockPlayerApiClient(final String baseUrl, final String apiKey,
                                  final String schema,
                                  final ObjectMapper objectMapper,
                                  final ExceptionHandler exceptionHandler) {
        super(baseUrl, apiKey, schema, objectMapper, exceptionHandler, BedrockPlayer.class);
    }

    public Optional<PlayerStats<BedrockPlayer>> getPlayerStats(final String playerName,
                                                               final boolean includeEmptyEntries,
                                                               final Set<Reason> filterReasons) throws InvalidPlayerNameRestException {
        return this.getPlayerStats(
                playerName,
                includeEmptyEntries,
                ZonedDateTime.now(),
                filterReasons
        );
    }

    public Optional<PlayerStats<BedrockPlayer>> getPlayerStats(final String playerName,
                                                               final boolean includeEmptyEntries,
                                                               final ZonedDateTime saveTime,
                                                               final Set<Reason> filterReasons) throws InvalidPlayerNameRestException {
        final HttpUrl.Builder httpBuilder = HttpUrl.parse(this.getPlayerBaseUrl() + "/" + playerName + "/stats/game")
                .newBuilder();
        try {
            return this.getPlayerStats(httpBuilder, includeEmptyEntries, saveTime, filterReasons);
        } catch (final InvalidStatNameRestException | InvalidGameNameRestException | InvalidLeaderboardCombinationRestException | InvalidBoardNameException exception) {
            // Should never be thrown
            throw new UnknownApiException(exception);
        }
    }
}
