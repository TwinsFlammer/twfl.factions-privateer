package br.com.twinsflammer.factionscaribe.combat.listener;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author SrGutyerrez
 */
public class PlayerTeleportListener implements Listener {
    @EventHandler
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
