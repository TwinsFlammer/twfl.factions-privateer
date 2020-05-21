package br.com.twinsflammer.factionsprivateer.combat.listener;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
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

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

        if (teleportCause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && privateerUser.inCombat()) {
            Location from = event.getFrom();

            player.teleport(from);
        }
    }
}
