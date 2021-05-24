package de.timmi6790.mpstats.api.client.java.player.models;

import de.timmi6790.mpstats.api.client.common.player.models.Player;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;


@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor(force = true)
public class JavaPlayer extends Player {
    private final UUID uuid;

    public JavaPlayer(final String name, final UUID uuid) {
        super(name);
        this.uuid = uuid;
    }
}
