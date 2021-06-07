package de.timmi6790.mpstats.api.client.common.board;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.timmi6790.mpstats.api.client.AbstractApiClient;
import de.timmi6790.mpstats.api.client.common.board.deserializers.BoardDeserializer;
import de.timmi6790.mpstats.api.client.common.board.deserializers.InvalidBoardNameExceptionDeserializer;
import de.timmi6790.mpstats.api.client.common.board.exceptions.InvalidBoardNameException;
import de.timmi6790.mpstats.api.client.common.board.models.Board;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;
import de.timmi6790.mpstats.api.client.exception.ExceptionHandler;
import de.timmi6790.mpstats.api.client.exception.exceptions.UnknownApiException;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;

public class BoardApiClient extends AbstractApiClient {
    public BoardApiClient(final String baseUrl,
                          final String apiKey,
                          final String schema,
                          final ObjectMapper objectMapper,
                          final ExceptionHandler exceptionHandler) {
        super(baseUrl, apiKey, schema, objectMapper, exceptionHandler);

        this.getObjectMapper().registerModule(
                new SimpleModule()
                        .addDeserializer(Board.class, new BoardDeserializer(Board.class))
                        .addDeserializer(InvalidBoardNameException.class, new InvalidBoardNameExceptionDeserializer(InvalidBoardNameException.class))
        );

        exceptionHandler.registerException("board-1", InvalidBoardNameException.class);
    }

    protected String getBoardBaseUrl() {
        return this.getBaseSchemaUrl() + "/board";
    }

    public List<Board> getBoards() {
        final HttpUrl url = HttpUrl.parse(this.getBoardBaseUrl());
        return this.getResponse(
                this.constructGetRequest(url),
                new TypeReference<List<Board>>() {
                }
        ).orElseGet(ArrayList::new);
    }

    public Board getBoard(final String boardName) throws InvalidBoardNameException {
        final HttpUrl url = HttpUrl.parse(this.getBoardBaseUrl() + "/" + boardName);
        try {
            return this.getResponseThrow(
                    this.constructGetRequest(url),
                    Board.class
            );
        } catch (final InvalidBoardNameException e) {
            throw e;
        } catch (final BaseRestException baseRestException) {
            throw new UnknownApiException(baseRestException);
        }
    }

    public Board createBoard(final String boardName,
                             final String websiteName,
                             final String cleanName,
                             final int updateTime) {
        throw new UnsupportedOperationException();
    }
}
