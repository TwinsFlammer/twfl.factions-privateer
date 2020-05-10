package com.redefocus.factionscaribe.specialitem.listener;

import com.redefocus.factionscaribe.specialitem.data.AbstractSpecialItem;
import com.redefocus.factionscaribe.specialitem.manager.SpecialItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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

        System.out.println("validando item especial");

        if (abstractSpecialItem == null) return;

        System.out.println("é válido -- validando consumer");

        if (abstractSpecialItem.getEventConsumer() != null) {
            System.out.println("Tem consumer");

            Consumer<PlayerInteractEvent> eventConsumer = abstractSpecialItem.getEventConsumer();

            System.out.println("validno instanceof do consumer");

            System.out.println("event: " + event.hashCode());

            System.out.println("hash: " + eventConsumer.hashCode());



            System.out.println("É válido e vai aceitar");

            eventConsumer.accept(event);
        }
    }
}
