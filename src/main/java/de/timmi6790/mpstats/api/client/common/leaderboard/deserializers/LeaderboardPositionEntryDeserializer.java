package de.timmi6790.mpstats.api.client.common.leaderboard.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.LeaderboardEntry;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.LeaderboardPositionEntry;
import de.timmi6790.mpstats.api.client.common.player.models.Player;

import java.io.IOException;
import java.io.Serial;

public class LeaderboardPositionEntryDeserializer<P extends Player> extends StdDeserializer<LeaderboardPositionEntry<P>> {
    @Serial
    private static final long serialVersionUID = -7925438362786185063L;

    private final JavaType leaderboardEntryType;

    public LeaderboardPositionEntryDeserializer(final JavaType valueType, final JavaType leaderboardEntryType) {
        super(valueType);

        this.leaderboardEntryType = leaderboardEntryType;
    }

    @Override
    public LeaderboardPositionEntry<P> deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final LeaderboardEntry<P> entry = ctxt.readValue(node.traverse(jsonParser.getCodec()), this.leaderboardEntryType);
        return new LeaderboardPositionEntry<>(
                entry,
                node.get("position").intValue()
        );
    }
}
