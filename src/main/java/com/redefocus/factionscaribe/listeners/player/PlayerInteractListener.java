package com.redefocus.factionscaribe.listeners.player;

import com.redefocus.common.shared.util.TimeFormatter;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
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

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

        ItemStack itemStack = event.getItem();

        Action action = event.getAction();

        if (itemStack.getType() == Material.ENDER_PEARL && action.name().contains("RIGHT_")) {
            if (!caribeUser.canUseEnderPearlListener()) {
                event.setCancelled(true);

                player.sendMessage(
                        String.format(
                                "§cAguarde %s para usar uma pérola do fim novamente.",
                                TimeFormatter.formatMinimized(caribeUser.getLastEnderPearlTeleportTime())
                        )
                );
            } else caribeUser.setLastEnderPearlTeleportTime(System.currentTimeMillis());
        }
    }
}
