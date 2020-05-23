package br.com.twinsflammer.factionsprivateer.listeners.general;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.Arrays;

/**
 * @author SrGutyerrez
 */
public class WorldListeners implements Listener {
    private final EntityType[] NO_GRAVITY_ENTITIES = {
            EntityType.FALLING_BLOCK,
    };

    private final Material[] NO_GRAVITY_MATERIALS = {
            Material.ANVIL
    };

    @EventHandler
    public void onSpread(BlockIgniteEvent event) {
        BlockIgniteEvent.IgniteCause igniteCause = event.getCause();

        if (igniteCause == BlockIgniteEvent.IgniteCause.SPREAD)
            event.setCancelled(true);
    }

    @EventHandler
    public void onChange(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        Block block = event.getBlock();

        if (Arrays.asList(this.NO_GRAVITY_ENTITIES).contains(entity.getType())) {
            BlockState state = block.getState();

            state.update();
            entity.remove();
            state.update();
            event.setCancelled(true);
            state.update();
        }

        if (Arrays.asList(this.NO_GRAVITY_MATERIALS).contains(block.getType()))
            event.setCancelled(true);
    }
}
