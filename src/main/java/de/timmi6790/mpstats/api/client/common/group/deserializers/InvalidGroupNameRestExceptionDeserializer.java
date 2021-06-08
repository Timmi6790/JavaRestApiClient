package de.timmi6790.mpstats.api.client.common.group.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.group.exceptions.InvalidGroupNameRestException;
import de.timmi6790.mpstats.api.client.common.group.models.Group;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class InvalidGroupNameRestExceptionDeserializer extends StdDeserializer<InvalidGroupNameRestException> {
    @Serial
    private static final long serialVersionUID = 1927719868056440603L;

    public InvalidGroupNameRestExceptionDeserializer(final Class<InvalidGroupNameRestException> vc) {
        super(vc);
    }

    @Override
    public InvalidGroupNameRestException deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final BaseRestException baseException = ctxt.readValue(node.traverse(jsonParser.getCodec()), BaseRestException.class);
        final List<Group> suggestedGroups = new ArrayList<>();
        for (final JsonNode subNodes : node.findValue("suggestedGroups")) {
            final Group group = ctxt.readValue(subNodes.traverse(jsonParser.getCodec()), Group.class);
            suggestedGroups.add(group);
        }

        return new InvalidGroupNameRestException(
                baseException,
                suggestedGroups
        );
    }
}
