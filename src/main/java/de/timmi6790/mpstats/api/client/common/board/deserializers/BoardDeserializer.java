package de.timmi6790.mpstats.api.client.common.board.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.board.models.Board;

import java.io.IOException;
import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

public class BoardDeserializer extends StdDeserializer<Board> {
    @Serial
    private static final long serialVersionUID = 6181652270917546525L;

    public BoardDeserializer(final Class<Board> vc) {
        super(vc);
    }

    @Override
    public Board deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        // Alias names
        final Set<String> aliasNames = new HashSet<>();
        for (final JsonNode aliasNode : node.findValue("aliasNames")) {
            aliasNames.add(aliasNode.textValue());
        }

        return new Board(
                node.get("boardName").textValue(),
                node.get("cleanName").textValue(),
                node.get("updateTime").asInt(),
                aliasNames
        );
    }
}
