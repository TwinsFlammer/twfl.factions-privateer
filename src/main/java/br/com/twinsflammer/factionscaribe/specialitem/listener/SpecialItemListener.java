package br.com.twinsflammer.factionscaribe.specialitem.listener;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.specialitem.data.AbstractSpecialItem;
import br.com.twinsflammer.factionscaribe.specialitem.defaults.LauncherSpecialItem;
import br.com.twinsflammer.factionscaribe.specialitem.manager.SpecialItemManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

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
            BiConsumer<PlayerInteractEvent, Event> eventConsumer = abstractSpecialItem.getEventConsumer();

            eventConsumer.accept(event, null);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity.getType() != EntityType.PLAYER) return;

        EntityDamageEvent.DamageCause damageCause = event.getCause();

        if (damageCause == EntityDamageEvent.DamageCause.FALL) {
            Player player = (Player) entity;

            if (player.hasMetadata(LauncherSpecialItem.METADATA)) {
                player.removeMetadata(LauncherSpecialItem.METADATA, FactionsCaribe.getInstance());

                event.setCancelled(true);
            }
        }
    }
}
