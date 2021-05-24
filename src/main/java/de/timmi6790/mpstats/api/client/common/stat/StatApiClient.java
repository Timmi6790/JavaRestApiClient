package de.timmi6790.mpstats.api.client.common.stat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.AbstractApiClient;
import de.timmi6790.mpstats.api.client.common.stat.deserializers.StatDeserializer;
import de.timmi6790.mpstats.api.client.common.stat.models.Stat;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StatApiClient extends AbstractApiClient {
    public StatApiClient(final String baseUrl,
                         final String apiKey,
                         final String schema,
                         final ObjectMapper objectMapper) {
        super(baseUrl, apiKey, schema, objectMapper);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(Stat.class, new StatDeserializer(Stat.class))
        );
    }

    protected String getStatBaseUrl() {
        return this.getBaseSchemaUrl() + "/stat";
    }

    public List<Stat> getStats() {
        final HttpUrl url = HttpUrl.parse(this.getStatBaseUrl());
        return this.getGetResponse(
                url,
                new TypeReference<List<Stat>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<Stat> getStat(final String statName) {
        final HttpUrl url = HttpUrl.parse(this.getStatBaseUrl() + "/" + statName);
        return this.getGetResponse(
                url,
                Stat.class
        );
    }

    public Stat createStat(final String statName,
                           final String websiteName,
                           final String cleanName,
                           final boolean isAchievement) {
        throw new UnsupportedOperationException();
    }
}
