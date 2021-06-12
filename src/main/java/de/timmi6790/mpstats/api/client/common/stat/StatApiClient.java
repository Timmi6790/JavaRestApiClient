package de.timmi6790.mpstats.api.client.common.stat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.AbstractApiClient;
import de.timmi6790.mpstats.api.client.common.stat.deserializers.InvalidStatNameRestExceptionDeserializer;
import de.timmi6790.mpstats.api.client.common.stat.deserializers.StatDeserializer;
import de.timmi6790.mpstats.api.client.common.stat.exceptions.InvalidStatNameRestException;
import de.timmi6790.mpstats.api.client.common.stat.models.Stat;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import de.timmi6790.mpstats.api.client.exception.ExceptionHandler;
import de.timmi6790.mpstats.api.client.exception.exceptions.UnknownApiException;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;

public class StatApiClient extends AbstractApiClient {
    public StatApiClient(final String baseUrl,
                         final String apiKey,
                         final String schema,
                         final ObjectMapper objectMapper,
                         final ExceptionHandler exceptionHandler) {
        super(baseUrl, apiKey, schema, objectMapper, exceptionHandler);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(Stat.class, new StatDeserializer(Stat.class))
                        .addDeserializer(InvalidStatNameRestException.class, new InvalidStatNameRestExceptionDeserializer())
        );

        exceptionHandler.registerException("stat-1", InvalidStatNameRestException.class);
    }

    protected String getStatBaseUrl() {
        return this.getBaseSchemaUrl() + "/stat";
    }

    public List<Stat> getStats() {
        final HttpUrl url = HttpUrl.parse(this.getStatBaseUrl());
        return this.getResponse(
                this.constructGetRequest(url),
                new TypeReference<List<Stat>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Stat getStat(final String statName) throws InvalidStatNameRestException {
        final HttpUrl url = HttpUrl.parse(this.getStatBaseUrl() + "/" + statName);
        try {
            return this.getResponseThrow(
                    this.constructGetRequest(url),
                    Stat.class
            );
        } catch (final InvalidStatNameRestException e) {
            throw e;
        } catch (final BaseRestException baseRestException) {
            throw new UnknownApiException(baseRestException);
        }
    }

    public Stat createStat(final String statName,
                           final String websiteName,
                           final String cleanName,
                           final boolean isAchievement) {
        throw new UnsupportedOperationException();
    }
}
