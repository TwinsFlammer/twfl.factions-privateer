package br.com.twinsflammer.factionsprivateer.spawner.stacker.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Arrays;

/**
 * @author SrGutyerrez
 */
public class CreatureSpawnListener implements Listener {
    private final CreatureSpawnEvent.SpawnReason[] ALLOWED_REASONS = {
            CreatureSpawnEvent.SpawnReason.SPAWNER,
            CreatureSpawnEvent.SpawnReason.CUSTOM
    };

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onSpawn(CreatureSpawnEvent event) {
        CreatureSpawnEvent.SpawnReason spawnReason = event.getSpawnReason();

        if (!Arrays.asList(this.ALLOWED_REASONS).contains(spawnReason))
            event.setCancelled(true);
    }
}
