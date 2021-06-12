package de.timmi6790.mpstats.api.client.common.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.AbstractApiClient;
import de.timmi6790.mpstats.api.client.common.board.exceptions.InvalidBoardNameException;
import de.timmi6790.mpstats.api.client.common.filter.deserializers.FilterDeserializer;
import de.timmi6790.mpstats.api.client.common.filter.deserializers.FilterDurationDeserializer;
import de.timmi6790.mpstats.api.client.common.filter.models.Filter;
import de.timmi6790.mpstats.api.client.common.filter.models.FilterDuration;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.common.game.exceptions.InvalidGameNameRestException;
import de.timmi6790.mpstats.api.client.common.leaderboard.exceptions.InvalidLeaderboardCombinationRestException;
import de.timmi6790.mpstats.api.client.common.player.exceptions.InvalidPlayerNameRestException;
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

public class FilterApiClient<P extends Player> extends AbstractApiClient {
    private final Class<P> playerClass;

    public FilterApiClient(final String baseUrl,
                           final String apiKey,
                           final String schema,
                           final ObjectMapper objectMapper,
                           final ExceptionHandler exceptionHandler,
                           final Class<P> playerClass) {
        super(baseUrl, apiKey, schema, objectMapper, exceptionHandler);

        this.playerClass = playerClass;

        final JavaType filterType = this.getFilterType();
        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(Filter.class, new FilterDeserializer<>(filterType, playerClass))
                        .addDeserializer(FilterDuration.class, new FilterDurationDeserializer())
        );
    }

    protected final JavaType getFilterType() {
        return this.getObjectMapper().getTypeFactory().constructParametricType(Filter.class, this.playerClass);
    }

    protected String getFilterBaseUrl() {
        return this.getBaseSchemaUrl() + "/filter";
    }

    public List<Filter<P>> getFilters() {
        final HttpUrl url = HttpUrl.parse(this.getFilterBaseUrl());
        return this.getResponse(
                this.constructGetRequest(url),
                new TypeReference<List<Filter<P>>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<Filter<P>> createFilter(final String gameName,
                                            final String statName,
                                            final String boardName,
                                            final String playerName,
                                            final Reason reason,
                                            final ZonedDateTime filterStart,
                                            final ZonedDateTime filterEnd) throws InvalidStatNameRestException, InvalidGameNameRestException, InvalidPlayerNameRestException, InvalidLeaderboardCombinationRestException, InvalidBoardNameException {
        final HttpUrl url = HttpUrl.parse(this.getFilterBaseUrl() + "/" + gameName + "/" + statName + "/" + boardName + "/" + playerName)
                .newBuilder()
                .addQueryParameter("reason", reason.toString())
                .addQueryParameter("filterStart", filterStart.toString())
                .addQueryParameter("filterEnd", filterEnd.toString())
                .build();
        try {
            final Filter<P> filter = this.getResponseThrow(
                    this.constructPostRequest(url),
                    new TypeReference<>() {
                    }
            );
            return Optional.ofNullable(filter);
        } catch (final InvalidPlayerNameRestException | InvalidLeaderboardCombinationRestException | InvalidStatNameRestException | InvalidBoardNameException | InvalidGameNameRestException e) {
            throw e;
        } catch (final BaseRestException baseRestException) {
            throw new UnknownApiException(baseRestException);
        }
    }

    public void removeFilter(final String gameName,
                             final String statName,
                             final String boardName,
                             final String playerName) {
        //     @DeleteMapping("/{gameName}/{statName}/{boardName}/{playerName}")
        throw new UnsupportedOperationException();
    }
}
