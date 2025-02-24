package br.com.twinsflammer.factionsprivateer.security.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Recipe;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.Arrays;

/**
 * @author SrGutyerrez
 */
public class SecurityListener implements Listener {
    private final Material[] BLOCKED_MATERIALS = {
            Material.BED,
            Material.MINECART,
            Material.COMMAND_MINECART,
            Material.EXPLOSIVE_MINECART,
            Material.HOPPER_MINECART,
            Material.POWERED_MINECART,
            Material.STORAGE_MINECART,
            Material.BOAT,
            Material.NAME_TAG,
            Material.BEACON,
            Material.PISTON_BASE,
            Material.PISTON_EXTENSION,
            Material.PISTON_MOVING_PIECE,
            Material.PISTON_STICKY_BASE,
            Material.BOOK,
            Material.BOOK_AND_QUILL,
            Material.BOOKSHELF
    };

    private final EntityType[] BLOCKED_ENTITIES = {
            EntityType.MINECART,
            EntityType.MINECART_CHEST,
            EntityType.MINECART_COMMAND,
            EntityType.MINECART_FURNACE,
            EntityType.MINECART_HOPPER,
            EntityType.MINECART_MOB_SPAWNER,
            EntityType.MINECART_TNT,
            EntityType.BOAT,
            EntityType.PIG
    };

    private final InventoryType[] BLOCKED_INVENTORIES = {
            InventoryType.HOPPER,
            InventoryType.MERCHANT
    };

    @EventHandler
    public void onPrepare(PrepareItemCraftEvent event) {
        CraftingInventory craftingInventory = event.getInventory();
        Recipe recipe = craftingInventory.getRecipe();

        if (recipe != null && Arrays.asList(this.BLOCKED_MATERIALS).contains(recipe.getResult().getType()))
            craftingInventory.setResult(null);
    }

    @EventHandler
    public void onMount(EntityMountEvent event) {
        Entity entity = event.getMount();

        if (Arrays.asList(this.BLOCKED_ENTITIES).contains(entity.getType()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onCombust(EntityCombustEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();

        if (Arrays.asList(this.BLOCKED_INVENTORIES).contains(inventory.getType()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onFire(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEdit(PlayerEditBookEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();

        if (block.getType() == Material.ITEM_FRAME) {
            Player player = event.getPlayer();

            if (player.getVehicle() != null)
                event.setCancelled(true);
        }
    }
}
