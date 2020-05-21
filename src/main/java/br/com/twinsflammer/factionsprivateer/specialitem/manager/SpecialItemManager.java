package br.com.twinsflammer.factionsprivateer.specialitem.manager;

import com.google.common.collect.Lists;
import br.com.twinsflammer.api.spigot.TwinsPlugin;
import br.com.twinsflammer.common.shared.util.Printer;
import br.com.twinsflammer.factionsprivateer.specialitem.data.AbstractSpecialItem;
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

    public static void registerSpecialItem(AbstractSpecialItem abstractSpecialItem, TwinsPlugin focusPlugin) {
        SpecialItemManager.SPECIAL_ITEMS.add(abstractSpecialItem);

        Printer.INFO.coloredPrint("Registering special item " + abstractSpecialItem.getName() + " by " + focusPlugin.getName());
    }
}
