package de.timmi6790.mpstats.api.client.common.group;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.timmi6790.mpstats.api.client.AbstractApiClient;

public class GroupApiClient extends AbstractApiClient {
    public GroupApiClient(final String baseUrl,
                          final String apiKey,
                          final String schema,
                          final ObjectMapper objectMapper) {
        super(baseUrl, apiKey, schema, objectMapper);
    }
}
