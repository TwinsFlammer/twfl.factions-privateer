package com.redefocus.factionscaribe.specialitem.defaults;

import com.redefocus.api.spigot.inventory.item.CustomItem;
import com.redefocus.factionscaribe.specialitem.data.AbstractSpecialItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

/**
 * @author SrGutyerrez
 */
public class LauncherSpecialITem<E extends PlayerInteractEvent> extends AbstractSpecialItem<E> {
    public LauncherSpecialITem() {
        super(
                "lançador"
        );
    }

    @Override
    public ItemStack getItemStack() {
        return new CustomItem(Material.FIREWORK)
                .name("§bLançador")
                .lore("§7Começando o role")
                .build();
    }

    @Override
    public Consumer<E> getEventConsumer() {
        return (event) -> {
            Player player = event.getPlayer();

            event.setCancelled(true);

            Location location = player.getLocation();

            Vector vector = location.toVector();

            Vector vector1 = vector.clone();

            vector.multiply(2);

            vector.add(vector1);

            ItemStack itemStack = player.getItemInHand();

            if (itemStack.getAmount() > 1) {
                itemStack.setAmount(
                        itemStack.getAmount() - 1
                );
            } else player.setItemInHand(null);
        };
    }

    @Override
    public Class<? extends Event> getEventType() {
        return PlayerInteractEvent.class;
    }
}
