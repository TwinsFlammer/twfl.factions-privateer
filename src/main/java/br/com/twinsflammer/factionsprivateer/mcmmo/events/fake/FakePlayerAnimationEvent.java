package br.com.twinsflammer.factionsprivateer.mcmmo.events.fake;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;

/**
 * Called when handling extra drops to avoid issues with NoCheat.
 */
public class FakePlayerAnimationEvent extends PlayerAnimationEvent {

    public FakePlayerAnimationEvent(Player player) {
        super(player);
    }
}
