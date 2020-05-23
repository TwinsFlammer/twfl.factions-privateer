package br.com.twinsflammer.factionsprivateer.spawner.listener;

import br.com.twinsflammer.factionsprivateer.spawner.manager.SpawnerManager;
import br.com.twinsflammer.factionsprivateer.util.BlockUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author SrGutyerrez
 */
public class SpawnerBreakListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        Block block = event.getBlock();

        if (block.getType() != Material.MOB_SPAWNER) return;

        ItemStack itemStack = player.getItemInHand();

        if (itemStack != null && itemStack.getType().name().endsWith("_PICKAXE") && itemStack.containsEnchantment(Enchantment.SILK_TOUCH)) {
            ItemStack spawner = SpawnerManager.getSpawner(block);

            Location location = BlockUtil.getLocation(
                    block.getLocation(),
                    BlockUtil.getPlayerBlockFaceLooking(
                            player,
                            block
                    )
            );

            World world = location.getWorld();

            world.dropItemNaturally(
                    location,
                    spawner
            );

            block.setType(Material.AIR);
        }
    }
}
