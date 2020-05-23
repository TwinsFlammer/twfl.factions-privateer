package br.com.twinsflammer.factionsprivateer.spawner.listener;

import br.com.twinsflammer.factionsprivateer.protection.event.BlockExplodeEvent;
import br.com.twinsflammer.factionsprivateer.spawner.manager.SpawnerManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * @author SrGutyerrez
 */
public class SpawnerExplodeListener implements Listener {
    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        event.blockList()
                .stream()
                .filter(block -> block.getType() == Material.MOB_SPAWNER)
                .forEach(block -> {
                        if (this.canDropSpawner()) {
                            ItemStack spawner = SpawnerManager.getSpawner(block);

                            block.getDrops().add(spawner);
                            block.breakNaturally();
                        }
                });
    }

    private Boolean canDropSpawner() {
        Random random = new Random();

        return random.nextInt(6) == 2;
    }
}
