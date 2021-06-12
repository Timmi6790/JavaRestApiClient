package de.timmi6790.mpstats.api.client.common.player.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.board.models.Board;
import de.timmi6790.mpstats.api.client.common.game.models.Game;
import de.timmi6790.mpstats.api.client.common.player.models.GeneratedPlayerEntry;

import java.io.IOException;
import java.io.Serial;

public class GeneratedPlayerEntryDeserializer extends StdDeserializer<GeneratedPlayerEntry> {
    @Serial
    private static final long serialVersionUID = 709303619461306042L;

    public GeneratedPlayerEntryDeserializer() {
        super(GeneratedPlayerEntry.class);
    }

    @Override
    public GeneratedPlayerEntry deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final Game game = ctxt.readValue(node.get("game").traverse(jsonParser.getCodec()), Game.class);
        final Board board = ctxt.readValue(node.get("board").traverse(jsonParser.getCodec()), Board.class);

        return new GeneratedPlayerEntry(
                node.get("cleanStatName").textValue(),
                game,
                board,
                node.get("score").numberValue()
        );
    }
}
