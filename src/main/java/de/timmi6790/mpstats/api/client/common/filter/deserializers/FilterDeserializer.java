package de.timmi6790.mpstats.api.client.common.filter.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.filter.models.Filter;
import de.timmi6790.mpstats.api.client.common.filter.models.FilterDuration;
import de.timmi6790.mpstats.api.client.common.filter.models.Reason;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.Leaderboard;
import de.timmi6790.mpstats.api.client.common.player.models.Player;

import java.io.IOException;
import java.io.Serial;

public class FilterDeserializer<P extends Player> extends StdDeserializer<Filter<P>> {
    @Serial
    private static final long serialVersionUID = 4969492534198382479L;
    private final Class<P> playerClass;

    public FilterDeserializer(final JavaType valueType, final Class<P> playerClass) {
        super(valueType);

        this.playerClass = playerClass;
    }

    @Override
    public Filter<P> deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final P player = ctxt.readValue(node.get("player").traverse(jsonParser.getCodec()), this.playerClass);
        final Leaderboard leaderboard = ctxt.readValue(node.get("leaderboard").traverse(jsonParser.getCodec()), Leaderboard.class);
        final String reasonText = node.get("reason").asText();

        final boolean permanentFilter = node.get("permanent").booleanValue();
        final FilterDuration filterDuration;
        if (permanentFilter) {
            filterDuration = null;
        } else {
            filterDuration = ctxt.readValue(node.get("filterDuration").traverse(jsonParser.getCodec()), FilterDuration.class);
        }

        return new Filter<>(
                player,
                leaderboard,
                Reason.valueOf(reasonText),
                permanentFilter,
                filterDuration
        );
    }
}
