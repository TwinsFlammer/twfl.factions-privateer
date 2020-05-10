package com.redefocus.factionscaribe.specialitem.manager;

import com.google.common.collect.Lists;
import com.redefocus.factionscaribe.specialitem.data.AbstractSpecialItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author SrGutyerrez
 */
public class SpecialItemManager {
    private static List<AbstractSpecialItem> SPECIAL_ITEMS = Lists.newArrayList();

    public static AbstractSpecialItem getSpecialItem(ItemStack itemStack) {
        return SpecialItemManager.SPECIAL_ITEMS
                .stream()
                .filter(abstractSpecialItem -> abstractSpecialItem.getItemStack().isSimilar(itemStack))
                .findFirst()
                .orElse(null);
    }

    public static AbstractSpecialItem getSpecialItem(String name) {
        return SpecialItemManager.SPECIAL_ITEMS
                .stream()
                .filter(abstractSpecialItem -> abstractSpecialItem.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
