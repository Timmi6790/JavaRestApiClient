package de.timmi6790.mpstats.api.client.common.group;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.AbstractApiClient;
import de.timmi6790.mpstats.api.client.common.board.exceptions.InvalidBoardNameException;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.common.group.deserializers.GroupDeserializer;
import de.timmi6790.mpstats.api.client.common.group.deserializers.GroupPlayerStatsDeserializer;
import de.timmi6790.mpstats.api.client.common.group.deserializers.InvalidGroupNameRestExceptionDeserializer;
import de.timmi6790.mpstats.api.client.common.group.exceptions.InvalidGroupNameRestException;
import de.timmi6790.mpstats.api.client.common.group.models.Group;
import de.timmi6790.mpstats.api.client.common.group.models.GroupPlayerStats;
import de.timmi6790.mpstats.api.client.common.player.exceptions.InvalidPlayerNameRestException;
import de.timmi6790.mpstats.api.client.common.player.models.Player;
import de.timmi6790.mpstats.api.client.common.stat.exceptions.InvalidStatNameRestException;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import de.timmi6790.mpstats.api.client.exception.ExceptionHandler;
import de.timmi6790.mpstats.api.client.exception.exceptions.UnknownApiException;
import okhttp3.HttpUrl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GroupApiClient<P extends Player> extends AbstractApiClient {
    private final Class<P> playerClass;

    public GroupApiClient(final String baseUrl,
                          final String apiKey,
                          final String schema,
                          final ObjectMapper objectMapper,
                          final ExceptionHandler exceptionHandler,
                          final Class<P> playerClass) {
        super(baseUrl, apiKey, schema, objectMapper, exceptionHandler);

        this.playerClass = playerClass;

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(Group.class, new GroupDeserializer())
                        .addDeserializer(InvalidGroupNameRestException.class, new InvalidGroupNameRestExceptionDeserializer())
                        .addDeserializer(GroupPlayerStats.class, new GroupPlayerStatsDeserializer<>(this.getGroupPlayerStatsType(), playerClass))
        );

        this.getExceptionHandler()
                .registerException("group-1", InvalidGroupNameRestException.class);
    }

    protected String getGroupBaseUrl() {
        return this.getBaseSchemaUrl() + "/group";
    }

    protected final JavaType getGroupPlayerStatsType() {
        return this.getObjectMapper().getTypeFactory().constructParametricType(GroupPlayerStats.class, this.playerClass);
    }

    protected Optional<GroupPlayerStats<P>> getPlayerStats(final HttpUrl httpUrl,
                                                           final ZonedDateTime saveTime,
                                                           final Set<Reason> filterReasons,
                                                           final boolean includeEmptyEntries) throws InvalidGroupNameRestException, InvalidStatNameRestException, InvalidPlayerNameRestException, InvalidBoardNameException {
        final HttpUrl.Builder httpBuilder = httpUrl.newBuilder();
        httpBuilder.addQueryParameter("includeEmptyEntries", String.valueOf(includeEmptyEntries))
                .addQueryParameter("saveTime", saveTime.toString());
        this.addFilterReasons(httpBuilder, filterReasons);
        try {
            return Optional.ofNullable(
                    this.getResponseThrow(
                            this.constructGetRequest(httpBuilder.build()),
                            this.getGroupPlayerStatsType()
                    )
            );
        } catch (final InvalidGroupNameRestException | InvalidStatNameRestException | InvalidBoardNameException | InvalidPlayerNameRestException e) {
            throw e;
        } catch (final BaseRestException baseRestException) {
            throw new UnknownApiException(baseRestException);
        }
    }

    public List<Group> getGroups() {
        final HttpUrl url = HttpUrl.parse(this.getGroupBaseUrl());
        return this.getResponse(
                this.constructGetRequest(url),
                new TypeReference<List<Group>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<GroupPlayerStats<P>> getPlayerStats(final String groupName,
                                                        final String playerName,
                                                        final String statName,
                                                        final String boardName,
                                                        final Set<Reason> filterReasons,
                                                        final boolean includeEmptyEntries) throws InvalidPlayerNameRestException, InvalidStatNameRestException, InvalidBoardNameException, InvalidGroupNameRestException {
        return this.getPlayerStats(
                groupName,
                playerName,
                statName,
                boardName,
                ZonedDateTime.now(),
                filterReasons,
                includeEmptyEntries
        );
    }

    public Optional<GroupPlayerStats<P>> getPlayerStats(final String groupName,
                                                        final String playerName,
                                                        final String statName,
                                                        final String boardName,
                                                        final ZonedDateTime saveTime,
                                                        final Set<Reason> filterReasons,
                                                        final boolean includeEmptyEntries) throws InvalidPlayerNameRestException, InvalidStatNameRestException, InvalidBoardNameException, InvalidGroupNameRestException {
        final HttpUrl httpUrl = HttpUrl.parse(this.getGroupBaseUrl() + "/" + groupName + "/stat/player/" + playerName + "/" + statName + "/" + boardName);
        return this.getPlayerStats(
                httpUrl,
                saveTime,
                filterReasons,
                includeEmptyEntries
        );
    }
}
