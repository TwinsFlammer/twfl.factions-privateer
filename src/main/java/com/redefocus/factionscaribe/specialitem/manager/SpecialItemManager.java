package com.redefocus.factionscaribe.specialitem.manager;

import com.google.common.collect.Lists;
import com.redefocus.api.spigot.FocusPlugin;
import com.redefocus.common.shared.util.Printer;
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

    public static void registerSpecialItem(AbstractSpecialItem abstractSpecialItem, FocusPlugin focusPlugin) {
        SpecialItemManager.SPECIAL_ITEMS.add(abstractSpecialItem);

        Printer.INFO.coloredPrint("Registering special item " + abstractSpecialItem.getName() + " by " + focusPlugin.getName());
    }
}
