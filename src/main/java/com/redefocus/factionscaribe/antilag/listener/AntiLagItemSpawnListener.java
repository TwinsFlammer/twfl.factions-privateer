package com.redefocus.factionscaribe.antilag.listener;

import com.redefocus.factionscaribe.antilag.data.DroppedItem;
import com.redefocus.factionscaribe.antilag.manager.AntiLagManager;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

/**
 * @author SrGutyerrez
 */
public class AntiLagItemSpawnListener implements Listener {
    @EventHandler
    public void onSpawn(ItemSpawnEvent event) {
        Item item = event.getEntity();

        DroppedItem droppedItem = new DroppedItem(
                item,
                System.currentTimeMillis()
        );

        AntiLagManager.addDroppedItem(droppedItem);
    }
}
