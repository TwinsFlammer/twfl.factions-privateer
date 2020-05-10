package com.redefocus.factionscaribe.specialitem.defaults;

import com.redefocus.api.spigot.inventory.item.CustomItem;
import com.redefocus.factionscaribe.specialitem.data.AbstractSpecialItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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

            ItemStack itemStack = player.getItemInHand();

            if (itemStack != null && itemStack.isSimilar(this.getItemStack())) {
                player.sendMessage("Testando...");
            }
        };
    }

    @Override
    public Class<? extends Event> getEventType() {
        return PlayerInteractEvent.class;
    }
}
