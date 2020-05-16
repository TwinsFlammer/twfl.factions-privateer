package com.redefocus.factionscaribe.antilag.manager;

import com.google.common.collect.Lists;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.antilag.data.DroppedItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.List;

/**
 * @author SrGutyerrez
 */
public class AntiLagManager {
    private static final List<DroppedItem> DROPPED_ITEMS = Lists.newArrayList();

    public AntiLagManager() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(
                FactionsCaribe.getInstance(),
                () -> {
                    Iterator<DroppedItem> droppedItemIterator = AntiLagManager.DROPPED_ITEMS.iterator();

                    while (droppedItemIterator.hasNext()) {
                        DroppedItem droppedItem = droppedItemIterator.next();

                        if (droppedItem.canBeRemoved()) {
                            droppedItem.remove();
                            droppedItemIterator.remove();
                        } else {
                            droppedItem.updateDisplayName();
                        }
                    }
                },
                0,
                20L
        );
    }

    public static DroppedItem getDroppedItem(ItemStack itemStack) {
        return AntiLagManager.DROPPED_ITEMS.stream()
                .filter(droppedItem -> droppedItem.isSimilar(itemStack))
                .findFirst()
                .orElse(null);
    }

    public static void removeDroppedItem(DroppedItem droppedItem) {
        AntiLagManager.DROPPED_ITEMS.removeIf(droppedItem1 -> droppedItem1.equals(droppedItem));
    }

    public static void addDroppedItem(DroppedItem droppedItem) {
        AntiLagManager.DROPPED_ITEMS.add(droppedItem);
    }
}
