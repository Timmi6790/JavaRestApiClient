package de.timmi6790.mpstats.api.client.common.game.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.game.exceptions.InvalidGameCategoryNameRestException;
import de.timmi6790.mpstats.api.client.common.game.models.GameCategory;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class InvalidGameCategoryNameRestExceptionDeserializer extends StdDeserializer<InvalidGameCategoryNameRestException> {
    @Serial
    private static final long serialVersionUID = -4368576439344178083L;

    public InvalidGameCategoryNameRestExceptionDeserializer(final Class<InvalidGameCategoryNameRestException> vc) {
        super(vc);
    }

    @Override
    public InvalidGameCategoryNameRestException deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final BaseRestException baseException = ctxt.readValue(node.traverse(jsonParser.getCodec()), BaseRestException.class);
        final List<GameCategory> suggestedGameCategories = new ArrayList<>();
        for (final JsonNode categoryNode : node.findValue("suggestedGameCategories")) {
            final GameCategory gameCategory = ctxt.readValue(categoryNode.traverse(jsonParser.getCodec()), GameCategory.class);
            suggestedGameCategories.add(gameCategory);
        }

        return new InvalidGameCategoryNameRestException(
                baseException,
                suggestedGameCategories
        );
    }
}
