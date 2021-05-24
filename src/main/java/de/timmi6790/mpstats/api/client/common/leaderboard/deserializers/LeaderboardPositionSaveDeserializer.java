package de.timmi6790.mpstats.api.client.common.leaderboard.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.Leaderboard;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.LeaderboardPositionEntry;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.LeaderboardPositionSave;
import de.timmi6790.mpstats.api.client.common.player.models.Player;

import java.io.IOException;
import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardPositionSaveDeserializer<P extends Player> extends StdDeserializer<LeaderboardPositionSave<P>> {
    @Serial
    private static final long serialVersionUID = 7936090410670917365L;

    private final JavaType leaderboardPositionEntryType;

    public LeaderboardPositionSaveDeserializer(final JavaType valueType, final JavaType leaderboardPositionEntryType) {
        super(valueType);

        this.leaderboardPositionEntryType = leaderboardPositionEntryType;
    }

    @Override
    public LeaderboardPositionSave<P> deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final Leaderboard leaderboard = ctxt.readValue(node.get("leaderboard").traverse(jsonParser.getCodec()), Leaderboard.class);

        final List<LeaderboardPositionEntry<P>> entries = new ArrayList<>();
        for (final JsonNode entryNode : node.get("entries")) {
            entries.add(ctxt.readValue(entryNode.traverse(jsonParser.getCodec()), this.leaderboardPositionEntryType));
        }

        return new LeaderboardPositionSave<>(
                leaderboard,
                ZonedDateTime.parse(node.get("saveTime").asText()),
                entries
        );
    }
}
