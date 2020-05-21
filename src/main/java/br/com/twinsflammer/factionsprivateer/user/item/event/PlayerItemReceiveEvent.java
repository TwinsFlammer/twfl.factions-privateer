package br.com.twinsflammer.factionsprivateer.user.item.event;

import br.com.twinsflammer.api.spigot.event.TwinsEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public class PlayerItemReceiveEvent extends TwinsEvent {
    private final Player player;
    private final ItemStack itemStack;
}
