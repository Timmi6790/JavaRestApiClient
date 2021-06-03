package de.timmi6790.mpstats.api.client.common.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.AbstractApiClient;
import de.timmi6790.mpstats.api.client.common.filter.deserializers.FilterDeserializer;
import de.timmi6790.mpstats.api.client.common.filter.models.Filter;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.common.player.models.Player;
import de.timmi6790.mpstats.api.client.exception.ExceptionHandler;
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
        return this.getGetResponse(
                url,
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
                                            final ZonedDateTime filterEnd) {
        // @PostMapping("/{gameName}/{statName}/{boardName}/{playerName}")
        throw new UnsupportedOperationException();

    }

    public void removeFilter(final String gameName,
                             final String statName,
                             final String boardName,
                             final String playerName) {
        //     @DeleteMapping("/{gameName}/{statName}/{boardName}/{playerName}")
        throw new UnsupportedOperationException();
    }
}
