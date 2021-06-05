package de.timmi6790.mpstats.api.client.bedrock.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.timmi6790.mpstats.api.client.bedrock.player.models.BedrockPlayer;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.common.player.PlayerApiClient;
import de.timmi6790.mpstats.api.client.common.player.exceptions.InvalidPlayerNameRestException;
import de.timmi6790.mpstats.api.client.common.player.models.PlayerStats;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
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
                                                               final Set<Reason> filterReasons) throws InvalidPlayerNameRestException {
        return this.getPlayerStats(
                playerName,
                ZonedDateTime.now(),
                filterReasons
        );
    }

    public Optional<PlayerStats<BedrockPlayer>> getPlayerStats(final String playerName,
                                                               final ZonedDateTime saveTime,
                                                               final Set<Reason> filterReasons) throws InvalidPlayerNameRestException {
        final HttpUrl.Builder httpBuilder = HttpUrl.parse(this.getPlayerBaseUrl() + "/" + playerName + "/stats/game")
                .newBuilder()
                .addQueryParameter("saveTime", saveTime.toString());
        this.addFilterReasons(httpBuilder, filterReasons);
        try {
            return Optional.ofNullable(
                    this.getGetResponseThrow(
                            httpBuilder.build(),
                            this.getPlayerStatsType()
                    )
            );
        } catch (final InvalidPlayerNameRestException e) {
            throw e;
        } catch (final BaseRestException baseRestException) {
            throw new UnknownApiException(baseRestException);
        }
    }
}
