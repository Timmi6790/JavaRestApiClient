package de.timmi6790.mpstats.api.client.common.game.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.game.models.GameCategory;

import java.io.IOException;
import java.io.Serial;

public class GameCategoryDeserializer extends StdDeserializer<GameCategory> {
    @Serial
    private static final long serialVersionUID = -8006135647788462738L;

    public GameCategoryDeserializer(final Class<GameCategory> vc) {
        super(vc);
    }

    @Override
    public GameCategory deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        return new GameCategory(
                node.get("categoryName").asText()
        );
    }
}
