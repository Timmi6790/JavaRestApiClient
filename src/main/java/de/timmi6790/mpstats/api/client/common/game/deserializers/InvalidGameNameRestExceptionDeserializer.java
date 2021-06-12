package de.timmi6790.mpstats.api.client.common.game.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.game.exceptions.InvalidGameNameRestException;
import de.timmi6790.mpstats.api.client.common.game.models.Game;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class InvalidGameNameRestExceptionDeserializer extends StdDeserializer<InvalidGameNameRestException> {
    @Serial
    private static final long serialVersionUID = -5545362496906167112L;

    public InvalidGameNameRestExceptionDeserializer() {
        super(InvalidGameNameRestException.class);
    }

    @Override
    public InvalidGameNameRestException deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final BaseRestException baseException = ctxt.readValue(node.traverse(jsonParser.getCodec()), BaseRestException.class);
        final List<Game> suggestedGames = new ArrayList<>();
        for (final JsonNode gameNode : node.findValue("suggestedGames")) {
            final Game game = ctxt.readValue(gameNode.traverse(jsonParser.getCodec()), Game.class);
            suggestedGames.add(game);
        }

        return new InvalidGameNameRestException(
                baseException,
                suggestedGames
        );
    }
}
