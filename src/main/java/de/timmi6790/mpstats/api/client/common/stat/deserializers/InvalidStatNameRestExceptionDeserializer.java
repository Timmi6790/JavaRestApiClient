package de.timmi6790.mpstats.api.client.common.stat.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.stat.exceptions.InvalidStatNameRestException;
import de.timmi6790.mpstats.api.client.common.stat.models.Stat;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class InvalidStatNameRestExceptionDeserializer extends StdDeserializer<InvalidStatNameRestException> {
    @Serial
    private static final long serialVersionUID = 6965629702053851954L;

    public InvalidStatNameRestExceptionDeserializer(final Class<InvalidStatNameRestException> vc) {
        super(vc);
    }

    @Override
    public InvalidStatNameRestException deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final BaseRestException baseException = ctxt.readValue(node.traverse(jsonParser.getCodec()), BaseRestException.class);
        final List<Stat> suggestedStats = new ArrayList<>();
        for (final JsonNode subNodes : node.findValue("suggestedStats")) {
            final Stat stat = ctxt.readValue(subNodes.traverse(jsonParser.getCodec()), Stat.class);
            suggestedStats.add(stat);
        }

        return new InvalidStatNameRestException(
                baseException,
                suggestedStats
        );
    }
}
