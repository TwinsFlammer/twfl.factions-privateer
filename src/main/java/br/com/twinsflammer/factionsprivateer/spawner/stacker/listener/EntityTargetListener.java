package br.com.twinsflammer.factionsprivateer.spawner.stacker.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 * @author SrGutyerrez
 */
public class EntityTargetListener implements Listener {
    @EventHandler
    public void onTarget(EntityTargetEvent event) {
        Entity targetEntity = event.getTarget();

        if (targetEntity.getType() == EntityType.PLAYER)
            event.setCancelled(true);
    }
}
