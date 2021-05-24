package de.timmi6790.mpstats.api.client.common.player.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.player.models.GeneratedPlayerEntry;
import de.timmi6790.mpstats.api.client.common.player.models.Player;
import de.timmi6790.mpstats.api.client.common.player.models.PlayerEntry;
import de.timmi6790.mpstats.api.client.common.player.models.PlayerStats;

import java.io.IOException;
import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

public class PlayerStatsDeserializer<P extends Player> extends StdDeserializer<PlayerStats<P>> {
    @Serial
    private static final long serialVersionUID = -1249591357465322915L;

    private final Class<P> playerClass;

    public PlayerStatsDeserializer(final JavaType valueType, final Class<P> playerClass) {
        super(valueType);

        this.playerClass = playerClass;
    }

    @Override
    public PlayerStats<P> deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final P player = ctxt.readValue(node.get("player").traverse(jsonParser.getCodec()), this.playerClass);

        final Set<GeneratedPlayerEntry> generatedStats = new HashSet<>();
        for (final JsonNode statNode : node.findValue("generatedStats")) {
            generatedStats.add(ctxt.readValue(statNode.traverse(jsonParser.getCodec()), GeneratedPlayerEntry.class));
        }

        final Set<PlayerEntry> stats = new HashSet<>();
        for (final JsonNode statNode : node.findValue("stats")) {
            stats.add(ctxt.readValue(statNode.traverse(jsonParser.getCodec()), PlayerEntry.class));
        }

        return new PlayerStats<>(
                player,
                generatedStats,
                stats
        );
    }
}
