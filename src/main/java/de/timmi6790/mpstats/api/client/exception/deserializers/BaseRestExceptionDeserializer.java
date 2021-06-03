package de.timmi6790.mpstats.api.client.exception.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;

import java.io.IOException;
import java.io.Serial;
import java.time.ZonedDateTime;

public class BaseRestExceptionDeserializer extends StdDeserializer<BaseRestException> {
    @Serial
    private static final long serialVersionUID = 223367310279120396L;

    public BaseRestExceptionDeserializer(final Class<BaseRestException> vc) {
        super(vc);
    }

    @Override
    public BaseRestException deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        return new BaseRestException(
                ZonedDateTime.parse(node.get("timestamp").asText()),
                node.get("status").asInt(),
                node.get("error").asText(),
                node.get("path").asText(),
                node.get("message").asText()
        );
    }
}
