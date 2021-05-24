package de.timmi6790.mpstats.api.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.timmi6790.mpstats.api.client.exceptions.ApiException;
import de.timmi6790.mpstats.api.client.utilities.CheckedFunction;
import de.timmi6790.mpstats.api.client.utilities.OkHttpClientUtilities;
import lombok.AccessLevel;
import lombok.Getter;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Optional;

public abstract class AbstractApiClient {
    @Getter(AccessLevel.PROTECTED)
    private final ObjectMapper objectMapper;

    @Getter(AccessLevel.PROTECTED)
    private final OkHttpClient httpClient;

    private final String baseUrl;
    private final String schema;

    protected AbstractApiClient(final String baseUrl,
                                final String apiKey,
                                final String schema,
                                final ObjectMapper objectMapper) {
        this.baseUrl = baseUrl;
        this.schema = schema;

        this.objectMapper = objectMapper;
        this.httpClient = OkHttpClientUtilities.getOkHttpClient(apiKey);
    }

    protected String getBaseSchemaUrl() {
        return this.baseUrl + "/v1/" + this.schema;
    }

    protected Request constructGetRequest(final HttpUrl httpUrl) {
        return new Request.Builder()
                .url(httpUrl)
                .build();
    }

    protected <T> Optional<T> getGetResponse(final HttpUrl url, final Class<T> clazz) {
        final Request request = this.constructGetRequest(url);
        return this.getResponse(request, clazz);
    }

    protected <T> Optional<T> getGetResponse(final HttpUrl url, final TypeReference<T> typeToken) {
        final Request request = this.constructGetRequest(url);
        return this.getResponse(request, typeToken);
    }

    protected <T> Optional<T> getGetResponse(final HttpUrl url, final JavaType javaType) {
        final Request request = this.constructGetRequest(url);
        return this.getResponse(request, javaType);
    }

    protected <T> Optional<T> getResponse(final Request request, final Class<T> clazz) {
        return this.getResponse(
                request,
                bytes -> this.objectMapper.readValue(bytes, clazz)
        );
    }

    protected <T> Optional<T> getResponse(final Request request, final JavaType javaType) {
        return this.getResponse(
                request,
                bytes -> this.objectMapper.readValue(bytes, javaType)
        );
    }

    protected <T> Optional<T> getResponse(final Request request, final TypeReference<T> typeToken) {
        return this.getResponse(
                request,
                bytes -> this.objectMapper.readValue(bytes, typeToken)
        );
    }

    protected <T> Optional<T> getResponse(final Request request, final CheckedFunction<byte[], T> converterFunction) {
        try (final Response response = this.httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiException();
            }

            return Optional.ofNullable(converterFunction.apply(response.body().bytes()));
        } catch (final Exception e) {
            return Optional.empty();
        }
    }
}
