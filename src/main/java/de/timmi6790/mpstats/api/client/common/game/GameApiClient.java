package de.timmi6790.mpstats.api.client.common.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.AbstractApiClient;
import de.timmi6790.mpstats.api.client.common.game.deserializers.GameCategoryDeserializer;
import de.timmi6790.mpstats.api.client.common.game.deserializers.GameDeserializer;
import de.timmi6790.mpstats.api.client.common.game.deserializers.InvalidGameCategoryNameRestExceptionDeserializer;
import de.timmi6790.mpstats.api.client.common.game.deserializers.InvalidGameNameRestExceptionDeserializer;
import de.timmi6790.mpstats.api.client.common.game.exceptions.InvalidGameCategoryNameRestException;
import de.timmi6790.mpstats.api.client.common.game.exceptions.InvalidGameNameRestException;
import de.timmi6790.mpstats.api.client.common.game.models.Game;
import de.timmi6790.mpstats.api.client.common.game.models.GameCategory;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import de.timmi6790.mpstats.api.client.exception.ExceptionHandler;
import de.timmi6790.mpstats.api.client.exception.exceptions.UnknownApiException;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;

public class GameApiClient extends AbstractApiClient {
    public GameApiClient(final String baseUrl,
                         final String apiKey,
                         final String schema,
                         final ObjectMapper objectMapper,
                         final ExceptionHandler exceptionHandler) {
        super(baseUrl, apiKey, schema, objectMapper, exceptionHandler);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(Game.class, new GameDeserializer(Game.class))
                        .addDeserializer(GameCategory.class, new GameCategoryDeserializer(GameCategory.class))
                        .addDeserializer(InvalidGameNameRestException.class, new InvalidGameNameRestExceptionDeserializer(InvalidGameNameRestException.class))
                        .addDeserializer(InvalidGameCategoryNameRestException.class, new InvalidGameCategoryNameRestExceptionDeserializer(InvalidGameCategoryNameRestException.class))
        );

        exceptionHandler.registerException("game-1", InvalidGameNameRestException.class);
        exceptionHandler.registerException("game-2", InvalidGameCategoryNameRestException.class);
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

    public Game getGame(final String gameName) throws InvalidGameNameRestException {
        final HttpUrl url = HttpUrl.parse(this.getGameBaseUrl() + "/" + gameName);

        try {
            return this.getGetResponseThrow(
                    url,
                    Game.class
            );
        } catch (final InvalidGameNameRestException ex) {
            throw ex;
        } catch (final BaseRestException baseRestException) {
            throw new UnknownApiException(baseRestException);
        }
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

    public GameCategory getGameCategory(final String categoryName) throws InvalidGameCategoryNameRestException {
        final HttpUrl url = HttpUrl.parse(this.getGameCategoryBaseUrl() + "/" + categoryName);
        try {
            return this.getGetResponseThrow(
                    url,
                    GameCategory.class
            );
        } catch (final InvalidGameCategoryNameRestException e) {
            throw e;
        } catch (final BaseRestException baseRestException) {
            throw new UnknownApiException(baseRestException);
        }
    }
}
