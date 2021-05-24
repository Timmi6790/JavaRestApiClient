package de.timmi6790.mpstats.api.client.common.board;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.AbstractApiClient;
import de.timmi6790.mpstats.api.client.common.board.deserializers.BoardDeserializer;
import de.timmi6790.mpstats.api.client.common.board.models.Board;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardApiClient extends AbstractApiClient {
    public BoardApiClient(final String baseUrl,
                          final String apiKey,
                          final String schema,
                          final ObjectMapper objectMapper) {
        super(baseUrl, apiKey, schema, objectMapper);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(Board.class, new BoardDeserializer(Board.class))
        );
    }

    protected String getBoardBaseUrl() {
        return this.getBaseSchemaUrl() + "/board";
    }

    public List<Board> getBoards() {
        final HttpUrl url = HttpUrl.parse(this.getBoardBaseUrl());
        return this.getGetResponse(
                url,
                new TypeReference<List<Board>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Optional<Board> getBoard(final String boardName) {
        final HttpUrl url = HttpUrl.parse(this.getBoardBaseUrl() + "/" + boardName);
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
}
