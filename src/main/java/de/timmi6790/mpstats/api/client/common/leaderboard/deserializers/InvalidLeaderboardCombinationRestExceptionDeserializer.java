package de.timmi6790.mpstats.api.client.common.leaderboard.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.leaderboard.exceptions.InvalidLeaderboardCombinationRestException;
import de.timmi6790.mpstats.api.client.common.leaderboard.models.Leaderboard;
import de.timmi6790.mpstats.api.client.exception.BaseRestException;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class InvalidLeaderboardCombinationRestExceptionDeserializer extends StdDeserializer<InvalidLeaderboardCombinationRestException> {
    @Serial
    private static final long serialVersionUID = 3342025404018887507L;

    public InvalidLeaderboardCombinationRestExceptionDeserializer() {
        super(InvalidLeaderboardCombinationRestException.class);
    }

    @Override
    public InvalidLeaderboardCombinationRestException deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final BaseRestException baseException = ctxt.readValue(node.traverse(jsonParser.getCodec()), BaseRestException.class);
        final List<Leaderboard> suggestedLeaderboards = new ArrayList<>();
        for (final JsonNode subNodes : node.findValue("suggestedLeaderboards")) {
            final Leaderboard leaderboard = ctxt.readValue(subNodes.traverse(jsonParser.getCodec()), Leaderboard.class);
            suggestedLeaderboards.add(leaderboard);
        }

        return new InvalidLeaderboardCombinationRestException(
                baseException,
                suggestedLeaderboards
        );
    }
}
