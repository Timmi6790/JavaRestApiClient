package de.timmi6790.mpstats.api.client.common.filter.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.filter.models.FilterDuration;

import java.io.IOException;
import java.io.Serial;
import java.time.ZonedDateTime;

public class FilterDurationDeserializer extends StdDeserializer<FilterDuration> {
    @Serial
    private static final long serialVersionUID = -5924923576210715274L;

    public FilterDurationDeserializer() {
        super(FilterDuration.class);
    }

    @Override
    public FilterDuration deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        return new FilterDuration(
                ZonedDateTime.parse(node.get("start").asText()),
                ZonedDateTime.parse(node.get("end").asText())
        );
    }
}
