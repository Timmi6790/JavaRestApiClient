package de.timmi6790.mpstats.api.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import de.timmi6790.mpstats.api.client.exception.ExceptionHandler;
import de.timmi6790.mpstats.api.client.exception.exceptions.ApiOfflineException;
import de.timmi6790.mpstats.api.client.exception.exceptions.InvalidApiKeyException;
import de.timmi6790.mpstats.api.client.exception.exceptions.RateLimitException;
import de.timmi6790.mpstats.api.client.exception.exceptions.UnknownApiException;
import de.timmi6790.mpstats.api.client.utilities.CheckedFunction;
import de.timmi6790.mpstats.api.client.utilities.OkHttpClientUtilities;
import lombok.AccessLevel;
import lombok.Getter;
import okhttp3.*;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractApiClient {
    @Getter(AccessLevel.PROTECTED)
    private final ObjectMapper objectMapper;
    @Getter(AccessLevel.PROTECTED)
    private final ExceptionHandler exceptionHandler;

    @Getter(AccessLevel.PROTECTED)
    private final OkHttpClient httpClient;

    private final String baseUrl;
    private final String schema;

    protected AbstractApiClient(final String baseUrl,
                                final String apiKey,
                                final String schema,
                                final ObjectMapper objectMapper,
                                final ExceptionHandler exceptionHandler) {
        this.baseUrl = baseUrl;
        this.schema = schema;

        this.objectMapper = objectMapper;
        this.exceptionHandler = exceptionHandler;
        this.httpClient = OkHttpClientUtilities.getOkHttpClient(apiKey);
    }

    private <T> T parseResponse(final Response response, final CheckedFunction<byte[], T> converterFunction) throws BaseRestException, IOException {
        final byte[] responseBody = response.body().bytes();
        if (!response.isSuccessful()) {
            final JsonNode parent = this.objectMapper.readTree(responseBody);
            final JsonNode errorNode = parent.path("error");

            if (errorNode.isMissingNode()) {
                // TODO: Throw more specific exceptions
                throw new UnknownApiException("No error code found");
            }

            final String errorCode = errorNode.asText();
            if ("Too Many Requests".equals(errorCode)) {
                final String retryInSecondsHeader = response.header("x-rate-limit-retry-after-seconds");
                final ZonedDateTime retryTime;
                if (retryInSecondsHeader != null) {
                    final long retryInSeconds = Long.parseLong(retryInSecondsHeader);
                    retryTime = ZonedDateTime.now().plusSeconds(retryInSeconds);
                } else {
                    retryTime = ZonedDateTime.now();
                }

                throw new RateLimitException(retryTime);
            } else if ("Forbidden".equals(errorCode)) {
                throw new InvalidApiKeyException();
            }

            final Optional<Class<? extends BaseRestException>> exceptionClassOpt = this.exceptionHandler.getRestExceptionClass(errorCode);
            if (exceptionClassOpt.isPresent()) {
                throw this.objectMapper.readValue(responseBody, exceptionClassOpt.get());
            }

            // TODO: Throw more specific exceptions
            throw new UnknownApiException("Invalid error code of " + errorCode);
        }

        return converterFunction.apply(responseBody);
    }

    protected String getBaseSchemaUrl() {
        return this.baseUrl + "/v1/" + this.schema;
    }

    protected void addFilterReasons(final HttpUrl.Builder httpBuilder, final Set<Reason> filterReasons) {
        for (final Reason reason : filterReasons) {
            httpBuilder.addQueryParameter("filterReasons", reason.toString());
        }
    }

    protected Request constructPostRequest(final HttpUrl httpUrl) {
        return this.constructPostRequest(httpUrl, RequestBody.create("", null));
    }

    protected Request constructPostRequest(final HttpUrl httpUrl, final RequestBody requestBody) {
        return new Request.Builder()
                .url(httpUrl)
                .post(requestBody)
                .build();
    }

    protected Request constructGetRequest(final HttpUrl httpUrl) {
        return new Request.Builder()
                .url(httpUrl)
                .build();
    }

    protected <T> Optional<T> getResponse(final Request request, final Class<T> clazz) {
        return this.parseRequest(
                request,
                bytes -> this.objectMapper.readValue(bytes, clazz)
        );
    }

    protected <T> Optional<T> getResponse(final Request request, final TypeReference<T> typeToken) {
        return this.parseRequest(
                request,
                bytes -> this.objectMapper.readValue(bytes, typeToken)
        );
    }

    protected <T> Optional<T> getResponse(final Request request, final JavaType javaType) {
        return this.parseRequest(
                request,
                bytes -> this.objectMapper.readValue(bytes, javaType)
        );
    }

    protected <T> Optional<T> parseRequest(final Request request, final CheckedFunction<byte[], T> converterFunction) {
        try {
            return Optional.ofNullable(this.parseRequestThrow(request, converterFunction));
        } catch (final InvalidApiKeyException | RateLimitException | ApiOfflineException exception) {
            throw exception;
        } catch (final Exception exception) {
            return Optional.empty();
        }
    }

    protected <T> T getResponseThrow(final Request request, final Class<T> clazz) throws BaseRestException {
        return this.parseRequestThrow(
                request,
                bytes -> this.objectMapper.readValue(bytes, clazz)
        );
    }

    protected <T> T getResponseThrow(final Request request, final TypeReference<T> typeToken) throws BaseRestException {
        return this.parseRequestThrow(
                request,
                bytes -> this.objectMapper.readValue(bytes, typeToken)
        );
    }

    protected <T> T getResponseThrow(final Request request, final JavaType javaType) throws BaseRestException {
        return this.parseRequestThrow(
                request,
                bytes -> this.objectMapper.readValue(bytes, javaType)
        );
    }

    protected <T> T parseRequestThrow(final Request request, final CheckedFunction<byte[], T> converterFunction) throws BaseRestException {
        try (final Response response = this.httpClient.newCall(request).execute()) {
            return this.parseResponse(response, converterFunction);
        } catch (final SocketTimeoutException | ConnectException e) {
            throw new ApiOfflineException();
        } catch (final IOException e) {
            throw new UnknownApiException(e);
        }
    }
}
