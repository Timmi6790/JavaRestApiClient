package de.timmi6790.mpstats.api.client.common.player.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.player.exceptions.InvalidPlayerNameRestException;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;

import java.io.IOException;
import java.io.Serial;

public class InvalidPlayerNameRestExceptionDeserializer extends StdDeserializer<InvalidPlayerNameRestException> {
    @Serial
    private static final long serialVersionUID = 1847756821870120579L;

    public InvalidPlayerNameRestExceptionDeserializer() {
        super(InvalidPlayerNameRestException.class);
    }

    @Override
    public InvalidPlayerNameRestException deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final BaseRestException baseException = ctxt.readValue(node.traverse(jsonParser.getCodec()), BaseRestException.class);

        return new InvalidPlayerNameRestException(baseException);
    }
}
