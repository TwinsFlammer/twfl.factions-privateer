package br.com.twinsflammer.factionsprivateer.listeners.player;

import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.spawn.manager.SpawnManager;
import br.com.twinsflammer.api.spigot.teleport.data.TeleportRequest;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * @author SrGutyerrez
 */
public class PlayerRespawnListener implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Integer serverId = SpigotAPI.getCurrentServer().getId(),
                rootServerId = SpigotAPI.getRootServerId();

        Location location = SpawnManager.DEFAULT_SPAWN;

        if (serverId.equals(rootServerId)) {
            if (location != null)
                player.teleport(location);
        } else {
            PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

            TeleportRequest teleportRequest = new TeleportRequest(
                    privateerUser.getId(),
                    null,
                    location,
                    rootServerId,
                    privateerUser.getTeleportTime()
            );

            teleportRequest.start();
        }
    }
}
