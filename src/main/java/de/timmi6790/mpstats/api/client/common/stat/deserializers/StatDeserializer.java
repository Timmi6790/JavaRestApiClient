package de.timmi6790.mpstats.api.client.common.stat.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.stat.models.Stat;

import java.io.IOException;
import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

public class StatDeserializer extends StdDeserializer<Stat> {
    @Serial
    private static final long serialVersionUID = -6678170098870678646L;

    public StatDeserializer(final Class<Stat> vc) {
        super(vc);
    }

    @Override
    public Stat deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        // Alias names
        final Set<String> aliasNames = new HashSet<>();
        for (final JsonNode aliasNode : node.findValue("aliasNames")) {
            aliasNames.add(aliasNode.textValue());
        }

        return new Stat(
                node.get("statName").textValue(),
                node.get("cleanName").textValue(),
                node.get("description").textValue(),
                node.get("achievement").asBoolean(),
                aliasNames
        );
    }
}
