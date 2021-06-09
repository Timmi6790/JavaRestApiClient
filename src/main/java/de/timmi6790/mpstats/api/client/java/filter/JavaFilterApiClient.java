package de.timmi6790.mpstats.api.client.java.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.timmi6790.mpstats.api.client.common.board.exceptions.InvalidBoardNameException;
import de.timmi6790.mpstats.api.client.common.filter.FilterApiClient;
import de.timmi6790.mpstats.api.client.common.filter.models.Filter;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.common.game.exceptions.InvalidGameNameRestException;
import de.timmi6790.mpstats.api.client.common.leaderboard.exceptions.InvalidLeaderboardCombinationRestException;
import de.timmi6790.mpstats.api.client.common.stat.exceptions.InvalidStatNameRestException;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import de.timmi6790.mpstats.api.client.exception.ExceptionHandler;
import de.timmi6790.mpstats.api.client.exception.exceptions.UnknownApiException;
import de.timmi6790.mpstats.api.client.java.player.models.JavaPlayer;
import okhttp3.HttpUrl;

import java.util.Optional;
import java.util.UUID;

public class JavaFilterApiClient extends FilterApiClient<JavaPlayer> {
    public JavaFilterApiClient(final String baseUrl, final String apiKey, final String schema, final ObjectMapper objectMapper, final ExceptionHandler exceptionHandler) {
        super(baseUrl, apiKey, schema, objectMapper, exceptionHandler, JavaPlayer.class);
    }

    public Optional<Filter<JavaPlayer>> createPermanentFilter(final String gameName,
                                                              final String statName,
                                                              final String boardName,
                                                              final UUID playerUUID,
                                                              final Reason reason) throws InvalidStatNameRestException, InvalidGameNameRestException, InvalidLeaderboardCombinationRestException, InvalidBoardNameException {
        final HttpUrl url = HttpUrl.parse(this.getFilterBaseUrl() + "/permanent/" + gameName + "/" + statName + "/" + boardName + "/uuid/" + playerUUID)
                .newBuilder()
                .addQueryParameter("reason", reason.toString())
                .build();
        try {
            final Filter<JavaPlayer> filter = this.getResponseThrow(
                    this.constructPostRequest(url),
                    new TypeReference<>() {
                    }
            );
            return Optional.ofNullable(filter);
        } catch (final InvalidLeaderboardCombinationRestException | InvalidStatNameRestException | InvalidBoardNameException | InvalidGameNameRestException e) {
            throw e;
        } catch (final BaseRestException baseRestException) {
            throw new UnknownApiException(baseRestException);
        }
    }
}
