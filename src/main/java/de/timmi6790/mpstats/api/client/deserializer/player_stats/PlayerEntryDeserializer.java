package de.timmi6790.mpstats.api.client.deserializer.player_stats;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.models.leaderboard.Leaderboard;
import de.timmi6790.mpstats.api.client.models.player_stats.PlayerEntry;

import java.io.IOException;
import java.io.Serial;
import java.time.ZonedDateTime;

public class PlayerEntryDeserializer extends StdDeserializer<PlayerEntry> {
    @Serial
    private static final long serialVersionUID = -5135974199630410038L;

    public PlayerEntryDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public PlayerEntry deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final Leaderboard leaderboard = ctxt.readValue(node.get("leaderboard").traverse(jsonParser.getCodec()), Leaderboard.class);
        return new PlayerEntry(
                leaderboard,
                ZonedDateTime.parse(node.get("saveTime").asText()),
                node.get("score").asLong(),
                node.get("position").asInt()
        );
    }
}
