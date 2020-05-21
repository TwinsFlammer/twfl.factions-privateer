package br.com.twinsflammer.factionsprivateer.antilag.listener;

import br.com.twinsflammer.factionsprivateer.antilag.data.DroppedItem;
import br.com.twinsflammer.factionsprivateer.antilag.manager.AntiLagManager;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * @author SrGutyerrez
 */
public class PlayerPickupDroppedItemListener implements Listener {
    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Item item = event.getItem();

        DroppedItem droppedItem = AntiLagManager.getDroppedItem(item.getItemStack());

        if (droppedItem != null) {
            AntiLagManager.removeDroppedItem(droppedItem);
        }
    }
}
