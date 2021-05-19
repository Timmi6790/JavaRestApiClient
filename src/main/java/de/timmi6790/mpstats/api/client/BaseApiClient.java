package de.timmi6790.mpstats.api.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import de.timmi6790.mpstats.api.client.bedrock.errors.ApiException;
import de.timmi6790.mpstats.api.client.deserializer.board.BoardDeserializer;
import de.timmi6790.mpstats.api.client.deserializer.game.GameDeserializer;
import de.timmi6790.mpstats.api.client.deserializer.leaderboard.LeaderboardDeserializer;
import de.timmi6790.mpstats.api.client.deserializer.player_stats.GeneratedPlayerEntryDeserializer;
import de.timmi6790.mpstats.api.client.deserializer.player_stats.PlayerEntryDeserializer;
import de.timmi6790.mpstats.api.client.deserializer.player_stats.PlayerStatsDeserializer;
import de.timmi6790.mpstats.api.client.deserializer.stat.StatDeserializer;
import de.timmi6790.mpstats.api.client.models.board.Board;
import de.timmi6790.mpstats.api.client.models.filter.Reason;
import de.timmi6790.mpstats.api.client.models.game.Game;
import de.timmi6790.mpstats.api.client.models.game.GameCategory;
import de.timmi6790.mpstats.api.client.models.leaderboard.Leaderboard;
import de.timmi6790.mpstats.api.client.models.player.Player;
import de.timmi6790.mpstats.api.client.models.player_stats.GeneratedPlayerEntry;
import de.timmi6790.mpstats.api.client.models.player_stats.PlayerEntry;
import de.timmi6790.mpstats.api.client.models.player_stats.PlayerStats;
import de.timmi6790.mpstats.api.client.models.stat.Stat;
import de.timmi6790.mpstats.api.client.utilities.CheckedFunction;
import lombok.Getter;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Getter
public abstract class BaseApiClient<P extends Player> {
    private static final String USER_AGENT = "JavaRestApiClient";

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new Jdk8Module())
            .addModule(new AfterburnerModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private final OkHttpClient httpClient;

    private final String baseUrl;
    private final String schema;

    private final Class<P> playerClass;

    protected BaseApiClient(final String baseUrl, final String apiKey, final String schema, final Class<P> playerClass) {
        this.baseUrl = baseUrl;
        this.schema = schema;

        this.playerClass = playerClass;

        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    final Request originalRequest = chain.request();

                    final Request.Builder newRequestBuilder = originalRequest.newBuilder()
                            .header("User-Agent", USER_AGENT);

                    if (apiKey != null && !apiKey.isEmpty()) {
                        newRequestBuilder.addHeader("X-Api-Key", apiKey);
                    }

                    return chain.proceed(newRequestBuilder.build());
                })
                .build();

        final JavaType playerStatsType = this.objectMapper.getTypeFactory().constructParametricType(PlayerStats.class, this.playerClass);
        this.objectMapper.registerModule(
                new SimpleModule()
                        .addDeserializer(Game.class, new GameDeserializer(Game.class))
                        .addDeserializer(Board.class, new BoardDeserializer(Board.class))
                        .addDeserializer(Stat.class, new StatDeserializer(Stat.class))
                        .addDeserializer(Leaderboard.class, new LeaderboardDeserializer(Leaderboard.class))
                        .addDeserializer(PlayerStats.class, new PlayerStatsDeserializer<>(playerStatsType, playerClass))
                        .addDeserializer(GeneratedPlayerEntry.class, new GeneratedPlayerEntryDeserializer(GeneratedPlayerEntry.class))
                        .addDeserializer(PlayerEntry.class, new PlayerEntryDeserializer(PlayerEntry.class))
        );
    }


    private String getBaseSchemaUrl() {
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

    // Games
    public List<Game> getGames() {
        final HttpUrl url = HttpUrl.parse(this.getBaseSchemaUrl() + "/game");
        return this.getGetResponse(
                url,
                new TypeReference<List<Game>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<Game> getGame(final String gameName) {
        final HttpUrl url = HttpUrl.parse(this.getBaseSchemaUrl() + "/game/" + gameName);
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
        final HttpUrl url = HttpUrl.parse(this.getBaseSchemaUrl() + "/game/category");
        return this.getGetResponse(
                url,
                new TypeReference<List<GameCategory>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<GameCategory> getGameCategory(final String categoryName) {
        final HttpUrl url = HttpUrl.parse(this.getBaseSchemaUrl() + "/game/category/" + categoryName);
        return this.getGetResponse(
                url,
                GameCategory.class
        );
    }

    // Stats
    public List<Stat> getStats() {
        final HttpUrl url = HttpUrl.parse(this.getBaseSchemaUrl() + "/stat");
        return this.getGetResponse(
                url,
                new TypeReference<List<Stat>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<Stat> getStat(final String statName) {
        final HttpUrl url = HttpUrl.parse(this.getBaseSchemaUrl() + "/stat/" + statName);
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

    // Boards
    public List<Board> getBoards() {
        final HttpUrl url = HttpUrl.parse(this.getBaseSchemaUrl() + "/board");
        return this.getGetResponse(
                url,
                new TypeReference<List<Board>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<Board> getBoard(final String boardName) {
        final HttpUrl url = HttpUrl.parse(this.getBaseSchemaUrl() + "/board/" + boardName);
        return this.getGetResponse(
                url,
                Board.class
        );
    }

    public Board createBoard(final String boardName,
                             final String websiteName,
                             final String cleanName,
                             final int updateTime) {
        throw new UnsupportedOperationException();
    }

    // Leaderboards
    public List<Leaderboard> getLeaderboards() {
        final HttpUrl url = HttpUrl.parse(this.getBaseSchemaUrl() + "/leaderboard");
        return this.getGetResponse(
                url,
                new TypeReference<List<Leaderboard>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<Leaderboard> getLeaderboard(final String gameName,
                                                final String statName,
                                                final String boardName) {
        final HttpUrl url = HttpUrl.parse(this.getBaseSchemaUrl() + "/leaderboard/" + gameName + "/" + statName + "/" + boardName);
        return this.getGetResponse(
                url,
                Leaderboard.class
        );
    }

    public Optional<Leaderboard> createdLeaderboard(final String gameName,
                                                    final String statName,
                                                    final String boardName,
                                                    final boolean deprecated) {
        // "/{gameName}/{statName}/{boardName}"
        throw new UnsupportedOperationException();
    }

    // Player stats
    public Optional<PlayerStats<P>> getPlayerGameStats(final String playerName,
                                                       final String gameName,
                                                       final String boardName,
                                                       final Set<Reason> filterReasons) {
        return this.getPlayerGameStats(
                playerName,
                gameName,
                boardName,
                ZonedDateTime.now(),
                filterReasons
        );
    }

    public Optional<PlayerStats<P>> getPlayerGameStats(final String playerName,
                                                       final String gameName,
                                                       final String boardName,
                                                       final ZonedDateTime saveTime,
                                                       final Set<Reason> filterReasons) {
        // TODO: Add filter reasons
        final HttpUrl url = HttpUrl.parse(this.getBaseSchemaUrl() + "/player/" + playerName + "/stats/game/" + gameName + "/" + boardName)
                .newBuilder()
                .addQueryParameter("saveTime", saveTime.toString())
                .build();
        final JavaType type = this.objectMapper.getTypeFactory().constructParametricType(PlayerStats.class, this.playerClass);
        return this.getGetResponse(
                url,
                type
        );
    }

    public Optional<PlayerStats<P>> getPlayerStatStats(final String playerName,
                                                       final String statName,
                                                       final String boardName,
                                                       final Set<Reason> filterReasons) {
        return this.getPlayerStatStats(
                playerName,
                statName,
                boardName,
                ZonedDateTime.now(),
                filterReasons
        );
    }

    public Optional<PlayerStats<P>> getPlayerStatStats(final String playerName,
                                                       final String statName,
                                                       final String boardName,
                                                       final ZonedDateTime saveTime,
                                                       final Set<Reason> filterReasons) {
        // TODO: Add filter reasons
        final HttpUrl url = HttpUrl.parse(this.getBaseSchemaUrl() + "/player/" + playerName + "/stats/stat/" + statName + "/" + boardName)
                .newBuilder()
                .addQueryParameter("saveTime", saveTime.toString())
                .build();
        return this.getGetResponse(
                url,
                new TypeReference<>() {
                }
        );
    }
}
