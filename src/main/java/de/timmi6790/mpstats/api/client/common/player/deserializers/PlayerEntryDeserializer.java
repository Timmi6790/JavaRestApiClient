package de.timmi6790.mpstats.api.client.common.player.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.Leaderboard;
import de.timmi6790.mpstats.api.client.common.player.models.PlayerEntry;

import java.io.IOException;
import java.io.Serial;
import java.time.ZonedDateTime;

public class PlayerEntryDeserializer extends StdDeserializer<PlayerEntry> {
    @Serial
    private static final long serialVersionUID = -5135974199630410038L;

    public PlayerEntryDeserializer() {
        super(PlayerEntry.class);
    }

    @Override
    public PlayerEntry deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
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
