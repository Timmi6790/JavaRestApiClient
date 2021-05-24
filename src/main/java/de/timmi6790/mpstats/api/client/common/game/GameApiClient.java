package de.timmi6790.mpstats.api.client.common.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.AbstractApiClient;
import de.timmi6790.mpstats.api.client.common.game.deserializers.GameCategoryDeserializer;
import de.timmi6790.mpstats.api.client.common.game.deserializers.GameDeserializer;
import de.timmi6790.mpstats.api.client.common.game.models.Game;
import de.timmi6790.mpstats.api.client.common.game.models.GameCategory;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameApiClient extends AbstractApiClient {
    public GameApiClient(final String baseUrl,
                         final String apiKey,
                         final String schema,
                         final ObjectMapper objectMapper) {
        super(baseUrl, apiKey, schema, objectMapper);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(Game.class, new GameDeserializer(Game.class))
                        .addDeserializer(GameCategory.class, new GameCategoryDeserializer(GameCategory.class))
        );
    }

    protected String getGameBaseUrl() {
        return this.getBaseSchemaUrl() + "/game";
    }

    protected String getGameCategoryBaseUrl() {
        return this.getBaseSchemaUrl() + "/game/category";
    }

    public List<Game> getGames() {
        final HttpUrl url = HttpUrl.parse(this.getGameBaseUrl());
        return this.getGetResponse(
                url,
                new TypeReference<List<Game>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<Game> getGame(final String gameName) {
        final HttpUrl url = HttpUrl.parse(this.getGameBaseUrl() + "/" + gameName);
        return this.getGetResponse(
                url,
                Game.class
        );
    }

    public Game createGame(final String gameName,
                           final String websiteName,
                           final String cleanName,
                           final String categoryName) {
        throw new UnsupportedOperationException();
    }

    public List<GameCategory> getGameCategories() {
        final HttpUrl url = HttpUrl.parse(this.getGameCategoryBaseUrl());
        return this.getGetResponse(
                url,
                new TypeReference<List<GameCategory>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<GameCategory> getGameCategory(final String categoryName) {
        final HttpUrl url = HttpUrl.parse(this.getGameCategoryBaseUrl() + "/" + categoryName);
        return this.getGetResponse(
                url,
                GameCategory.class
        );
    }
}
