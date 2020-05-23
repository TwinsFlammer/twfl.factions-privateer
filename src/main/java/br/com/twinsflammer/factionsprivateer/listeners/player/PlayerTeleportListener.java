package br.com.twinsflammer.factionsprivateer.listeners.player;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
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

        Location toLocation = event.getTo();

        World world = toLocation.getWorld();
        WorldBorder worldBorder = world.getWorldBorder();

        Double size = worldBorder.getSize();

        Boolean isAboveBorder = toLocation.getX() >= (size / 2) || toLocation.getZ() >= (size / 2);

        if (isAboveBorder)
            player.setHealth(0.0D);
    }
}
