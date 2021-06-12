package de.timmi6790.mpstats.api.client.common.board.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.board.exceptions.InvalidBoardNameException;
import de.timmi6790.mpstats.api.client.common.board.models.Board;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class InvalidBoardNameExceptionDeserializer extends StdDeserializer<InvalidBoardNameException> {
    @Serial
    private static final long serialVersionUID = -8284625479441841472L;

    public InvalidBoardNameExceptionDeserializer() {
        super(InvalidBoardNameException.class);
    }

    @Override
    public InvalidBoardNameException deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final BaseRestException baseException = ctxt.readValue(node.traverse(jsonParser.getCodec()), BaseRestException.class);
        final List<Board> suggestedBoards = new ArrayList<>();
        for (final JsonNode subNodes : node.findValue("suggestedBoards")) {
            final Board board = ctxt.readValue(subNodes.traverse(jsonParser.getCodec()), Board.class);
            suggestedBoards.add(board);
        }

        return new InvalidBoardNameException(
                baseException,
                suggestedBoards
        );
    }
}
