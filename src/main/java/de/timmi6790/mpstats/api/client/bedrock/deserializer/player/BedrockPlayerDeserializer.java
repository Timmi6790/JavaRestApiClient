package de.timmi6790.mpstats.api.client.bedrock.deserializer.player;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.bedrock.models.player.BedrockPlayer;

import java.io.IOException;
import java.io.Serial;

public class BedrockPlayerDeserializer extends StdDeserializer<BedrockPlayer> {
    @Serial
    private static final long serialVersionUID = 1972105464341747687L;

    public BedrockPlayerDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public BedrockPlayer deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return new BedrockPlayer(
                node.get("name").textValue()
        );
    }
}
