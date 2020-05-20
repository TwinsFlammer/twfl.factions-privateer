package br.com.twinsflammer.factionscaribe.listeners.player;

import br.com.twinsflammer.factionscaribe.specialitem.data.AbstractSpecialItem;
import br.com.twinsflammer.factionscaribe.specialitem.manager.SpecialItemManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author SrGutyerrez
 */
public class PlayerPutItemInAnvilListener implements Listener {
    @EventHandler
    public void onPut(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory inventory = event.getInventory(),
                    clickedInventory = event.getClickedInventory();

        if (inventory.getType() == InventoryType.ANVIL) {
            if (event.getClick() == ClickType.NUMBER_KEY || event.isShiftClick()) {
                event.setCancelled(true);
                return;
            }

            if (clickedInventory != null && clickedInventory.equals(player.getInventory())) return;

            ItemStack itemStack = event.getCursor();

            if (itemStack == null || itemStack.getType() == Material.AIR) return;

            AbstractSpecialItem abstractSpecialItem = SpecialItemManager.getSpecialItem(itemStack);

            if (abstractSpecialItem != null) {
                ItemStack cursor = event.getCursor();

                event.setCancelled(true);

                event.setCursor(null);

                player.closeInventory();

                player.getInventory().addItem(cursor);

                player.sendMessage("§cVocê não pode por itens especiais em uma bigorna.");
            }
        }
    }
}
