package com.redefocus.factionscaribe.security.listener;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
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
            Material.PISTON_STICKY_BASE
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

    public void onCombust(EntityCombustEvent event) {
        event.setCancelled(true);
    }
}
