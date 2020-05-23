package br.com.twinsflammer.factionsprivateer.listeners.player;

import br.com.twinsflammer.common.shared.util.TimeFormatter;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author SrGutyerrez
 */
public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

        ItemStack itemStack = event.getItem();

        if (itemStack == null) return;

        Action action = event.getAction();

        if (itemStack.getType() == Material.ENDER_PEARL && action.name().contains("RIGHT_")) {
            if (!privateerUser.canUseEnderPearlListener()) {
                event.setCancelled(true);

                player.sendMessage(
                        String.format(
                                "§cAguarde %s para usar uma pérola do fim novamente.",
                                TimeFormatter.formatMinimized(privateerUser.getLastEnderPearlTeleportTime() - System.currentTimeMillis())
                        )
                );
            } else privateerUser.setLastEnderPearlTeleportTime(System.currentTimeMillis());
        }
    }
}
