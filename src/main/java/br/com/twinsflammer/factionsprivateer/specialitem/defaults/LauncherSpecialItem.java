package br.com.twinsflammer.factionsprivateer.specialitem.defaults;

import br.com.twinsflammer.api.spigot.inventory.item.CustomItem;
import br.com.twinsflammer.factionsprivateer.specialitem.data.AbstractSpecialItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.function.BiConsumer;

/**
 * @author SrGutyerrez
 */
public class LauncherSpecialItem<E extends PlayerInteractEvent> extends AbstractSpecialItem<E> {
    public static final String METADATA = "LAUNCHER";

    public LauncherSpecialItem() {
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
    public BiConsumer<E, E> getEventConsumer() {
        return (event1, event2) -> {
            Player player = event1.getPlayer();

            event1.setCancelled(true);

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
