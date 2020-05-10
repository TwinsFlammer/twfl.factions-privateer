package com.redefocus.factionscaribe.specialitem.listener;

import com.redefocus.factionscaribe.specialitem.data.AbstractSpecialItem;
import com.redefocus.factionscaribe.specialitem.manager.SpecialItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @author SrGutyerrez
 */
public class SpecialItemListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack itemStack = player.getItemInHand();

        AbstractSpecialItem abstractSpecialItem = SpecialItemManager.getSpecialItem(itemStack);

        if (abstractSpecialItem == null) return;

        if (!abstractSpecialItem.getEventType().equals(PlayerInteractEvent.class)) return;

        if (abstractSpecialItem.getEventConsumer() != null) {
            Consumer<PlayerInteractEvent> eventConsumer = abstractSpecialItem.getEventConsumer();

            eventConsumer.accept(event);
        }
    }
}
