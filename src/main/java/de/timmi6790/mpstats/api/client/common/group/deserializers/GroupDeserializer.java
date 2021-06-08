package de.timmi6790.mpstats.api.client.common.group.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.timmi6790.mpstats.api.client.common.game.models.Game;
import de.timmi6790.mpstats.api.client.common.group.models.Group;

import java.io.IOException;
import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

public class GroupDeserializer extends StdDeserializer<Group> {
    @Serial
    private static final long serialVersionUID = -9033303933550042234L;

    public GroupDeserializer(final Class<Group> vc) {
        super(vc);
    }

    @Override
    public Group deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        // Alias names
        final Set<String> aliasNames = new HashSet<>();
        for (final JsonNode aliasNode : node.findValue("aliasNames")) {
            aliasNames.add(aliasNode.textValue());
        }

        // Games
        final Set<Game> games = new HashSet<>();
        for (final JsonNode gameNode : node.findValue("games")) {
            final Game game = ctxt.readValue(gameNode.traverse(jsonParser.getCodec()), Game.class);
            games.add(game);
        }

        return new Group(
                node.get("groupName").textValue(),
                node.get("cleanName").textValue(),
                node.get("description").textValue(),
                aliasNames,
                games
        );
    }
}
