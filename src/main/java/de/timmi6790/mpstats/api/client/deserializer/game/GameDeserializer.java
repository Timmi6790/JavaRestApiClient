package de.timmi6790.mpstats.api.client.deserializer.game;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.models.game.Game;

import java.io.IOException;
import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

public class GameDeserializer extends StdDeserializer<Game> {
    @Serial
    private static final long serialVersionUID = 8709573577191990693L;

    public GameDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public Game deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        // Alias names
        final Set<String> aliasNames = new HashSet<>();
        for (final JsonNode aliasNode : node.findValue("aliasNames")) {
            aliasNames.add(aliasNode.textValue());
        }

        return new Game(
                node.get("gameName").textValue(),
                node.get("cleanName").textValue(),
                aliasNames,
                node.get("categoryName").textValue(),
                node.get("description").textValue(),
                node.get("wikiUrl").textValue()
        );
    }
}
