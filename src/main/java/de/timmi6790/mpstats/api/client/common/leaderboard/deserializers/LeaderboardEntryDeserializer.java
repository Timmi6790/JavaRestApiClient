package de.timmi6790.mpstats.api.client.common.leaderboard.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.LeaderboardEntry;
import de.timmi6790.mpstats.api.client.common.player.models.Player;

import java.io.IOException;
import java.io.Serial;

public class LeaderboardEntryDeserializer<P extends Player> extends StdDeserializer<LeaderboardEntry<P>> {
    @Serial
    private static final long serialVersionUID = 8697356706001089860L;

    private final Class<P> playerClass;

    public LeaderboardEntryDeserializer(final JavaType valueType, final Class<P> playerClass) {
        super(valueType);

        this.playerClass = playerClass;
    }

    @Override
    public LeaderboardEntry<P> deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final P player = ctxt.readValue(node.get("player").traverse(jsonParser.getCodec()), this.playerClass);
        return new LeaderboardEntry<>(
                player,
                node.get("score").longValue()
        );
    }
}
