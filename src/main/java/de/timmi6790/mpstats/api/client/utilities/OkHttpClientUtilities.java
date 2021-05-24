package de.timmi6790.mpstats.api.client.utilities;

import lombok.experimental.UtilityClass;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class OkHttpClientUtilities {
    private final String USER_AGENT = "JavaRestApiClient";

    private final WeakHashMap<String, OkHttpClient> clients = new WeakHashMap<>();

    /**
     * Only construct a single client for each given apiKey
     *
     * @param apiKey the api key
     * @return the ok http client
     */
    public synchronized OkHttpClient getOkHttpClient(String apiKey) {
        if (apiKey == null) {
            apiKey = "";
        }

        OkHttpClient client = clients.get(apiKey);
        if (client != null) {
            return client;
        }

        final Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(100);
        dispatcher.setMaxRequestsPerHost(100);

        final ConnectionPool connectionPool = new ConnectionPool(
                100,
                60,
                TimeUnit.SECONDS
        );

        final String finalApiKey = apiKey;
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .addInterceptor(chain -> {
                    final Request originalRequest = chain.request();

                    final Request.Builder newRequestBuilder = originalRequest.newBuilder()
                            .header("User-Agent", USER_AGENT);

                    if (!finalApiKey.isEmpty()) {
                        newRequestBuilder.addHeader("X-Api-Key", finalApiKey);
                    }

                    return chain.proceed(newRequestBuilder.build());
                })
                .build();
        clients.put(apiKey, client);
        return client;
    }
}
