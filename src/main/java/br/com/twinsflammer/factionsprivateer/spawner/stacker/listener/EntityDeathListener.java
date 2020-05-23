package br.com.twinsflammer.factionsprivateer.spawner.stacker.listener;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Arrays;

/**
 * @author SrGutyerrez
 */
public class EntityDeathListener implements Listener {
    private final Material[] BLACKLISTED_TO_DROP = {
            Material.RED_ROSE,
            Material.ROTTEN_FLESH
    };

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        if (!Arrays.asList(SpawnerSpawnListener.ALLOWED_ENTITY_TYPES).contains(entity.getType()))
            return;

        Integer amount = entity.hasMetadata(SpawnerSpawnListener.STACK_METADATA) ? entity.getMetadata(SpawnerSpawnListener.STACK_METADATA).get(0).asInt() : 1;

        event.getDrops().forEach(itemStack -> {
            Material material = itemStack.getType();

            if (Arrays.asList(this.BLACKLISTED_TO_DROP).contains(material))
                itemStack.setType(Material.AIR);
            else {
                itemStack.setAmount(
                        itemStack.getAmount() * amount
                );
            }
        });
    }
}
