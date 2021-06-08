package de.timmi6790.mpstats.api.client.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import de.timmi6790.mpstats.api.client.common.board.BoardApiClient;
import de.timmi6790.mpstats.api.client.common.filter.FilterApiClient;
import de.timmi6790.mpstats.api.client.common.game.GameApiClient;
import de.timmi6790.mpstats.api.client.common.group.GroupApiClient;
import de.timmi6790.mpstats.api.client.common.leaderboard.LeaderboardApiClient;
import de.timmi6790.mpstats.api.client.common.player.PlayerApiClient;
import de.timmi6790.mpstats.api.client.common.player.models.Player;
import de.timmi6790.mpstats.api.client.common.stat.StatApiClient;
import de.timmi6790.mpstats.api.client.exception.ExceptionHandler;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class BaseApiClient<P extends Player> {
    @Getter(AccessLevel.PROTECTED)
    private final ObjectMapper objectMapper;

    @Getter(AccessLevel.PROTECTED)
    private final ExceptionHandler exceptionHandler;
    @Getter(AccessLevel.PROTECTED)
    private final String schema;

    private final BoardApiClient boardClient;
    private final FilterApiClient<P> filterClient;
    private final GameApiClient gameClient;
    private final GroupApiClient<P> groupClient;
    private final LeaderboardApiClient<P> leaderboardClient;
    private final PlayerApiClient<P> playerClient;
    private final StatApiClient statClient;

    public BaseApiClient(final String baseUrl, final String apiKey, final String schema, final Class<P> playerClass) {
        this.schema = schema;

        this.objectMapper = JsonMapper.builder()
                .addModule(new Jdk8Module())
                .addModule(new AfterburnerModule())
                .build();

        this.exceptionHandler = new ExceptionHandler(this.objectMapper);

        this.boardClient = new BoardApiClient(baseUrl, apiKey, schema, this.objectMapper, this.exceptionHandler);
        this.filterClient = new FilterApiClient<>(baseUrl, apiKey, schema, this.objectMapper, this.exceptionHandler, playerClass);
        this.gameClient = new GameApiClient(baseUrl, apiKey, schema, this.objectMapper, this.exceptionHandler);
        this.groupClient = new GroupApiClient<>(baseUrl, apiKey, schema, this.objectMapper, this.exceptionHandler, playerClass);
        this.leaderboardClient = new LeaderboardApiClient<>(baseUrl, apiKey, schema, this.objectMapper, this.exceptionHandler, playerClass);
        this.playerClient = new PlayerApiClient<>(baseUrl, apiKey, schema, this.objectMapper, this.exceptionHandler, playerClass);
        this.statClient = new StatApiClient(baseUrl, apiKey, schema, this.objectMapper, this.exceptionHandler);
    }
}
