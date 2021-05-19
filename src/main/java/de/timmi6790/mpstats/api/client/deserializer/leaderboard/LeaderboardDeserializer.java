package de.timmi6790.mpstats.api.client.deserializer.leaderboard;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.models.board.Board;
import de.timmi6790.mpstats.api.client.models.game.Game;
import de.timmi6790.mpstats.api.client.models.leaderboard.Leaderboard;
import de.timmi6790.mpstats.api.client.models.stat.Stat;

import java.io.IOException;
import java.io.Serial;
import java.time.ZonedDateTime;

public class LeaderboardDeserializer extends StdDeserializer<Leaderboard> {
    @Serial
    private static final long serialVersionUID = -6501827219870099245L;

    public LeaderboardDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public Leaderboard deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final Game game = ctxt.readValue(node.get("game").traverse(jsonParser.getCodec()), Game.class);
        final Stat stat = ctxt.readValue(node.get("stat").traverse(jsonParser.getCodec()), Stat.class);
        final Board board = ctxt.readValue(node.get("board").traverse(jsonParser.getCodec()), Board.class);

        return new Leaderboard(
                game,
                stat,
                board,
                node.get("deprecated").asBoolean(),
                ZonedDateTime.parse(node.get("lastSaveTime").asText()),
                ZonedDateTime.parse(node.get("lastCacheSaveTime").asText())
        );
    }
}
