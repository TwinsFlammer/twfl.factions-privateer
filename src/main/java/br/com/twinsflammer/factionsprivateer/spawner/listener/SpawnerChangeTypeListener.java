package br.com.twinsflammer.factionsprivateer.spawner.listener;

import br.com.twinsflammer.factionsprivateer.util.BlockUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author SrGutyerrez
 */
public class SpawnerChangeTypeListener implements Listener {
    @EventHandler
    public void onChange(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack itemStack = player.getItemInHand();

        Action action = event.getAction();

        if (itemStack == null || itemStack.getType() == Material.AIR
                || itemStack.getType() != Material.MONSTER_EGG || action != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();

        if (block.getType() != Material.MOB_SPAWNER) return;

        event.setCancelled(true);

        if (itemStack.getDurability() == 50) {
            Location location = block.getLocation();
            World world = location.getWorld();

            world.spawnEntity(
                    BlockUtil.getLocation(location, event.getBlockFace()),
                    EntityType.CREEPER
            );
        }
    }
}
