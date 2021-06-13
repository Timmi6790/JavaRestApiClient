package de.timmi6790.mpstats.api.client.common.group.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.group.models.Group;
import de.timmi6790.mpstats.api.client.common.group.models.GroupPlayerStats;
import de.timmi6790.mpstats.api.client.common.player.models.Player;
import de.timmi6790.mpstats.api.client.common.player.models.PlayerStats;

import java.io.IOException;
import java.io.Serial;

public class GroupPlayerStatsDeserializer<P extends Player> extends StdDeserializer<GroupPlayerStats<P>> {
    @Serial
    private static final long serialVersionUID = -2201038868705405890L;

    private final Class<P> playerClass;

    public GroupPlayerStatsDeserializer(final JavaType valueType, final Class<P> playerClass) {
        super(valueType);

        this.playerClass = playerClass;
    }

    @Override
    public GroupPlayerStats<P> deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final JavaType playerStatsType = ctxt.getTypeFactory().constructParametricType(PlayerStats.class, this.playerClass);
        final PlayerStats<P> playerStats = ctxt.readValue(node.traverse(jsonParser.getCodec()), playerStatsType);

        final Group group = ctxt.readValue(node.get("group").traverse(jsonParser.getCodec()), Group.class);

        return new GroupPlayerStats<>(
                playerStats,
                group
        );
    }
}
