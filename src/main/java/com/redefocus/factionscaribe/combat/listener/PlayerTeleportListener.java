package com.redefocus.factionscaribe.combat.listener;

import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author SrGutyerrez
 */
public class PlayerTeleportListener {
//    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        PlayerTeleportEvent.TeleportCause teleportCause = event.getCause();

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

        if (teleportCause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && caribeUser.inCombat()) {
            Location from = event.getFrom();

            player.teleport(from);
        }
    }
}
