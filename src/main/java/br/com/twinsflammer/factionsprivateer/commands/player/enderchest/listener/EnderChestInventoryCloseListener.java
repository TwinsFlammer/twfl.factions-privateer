package br.com.twinsflammer.factionsprivateer.commands.player.enderchest.listener;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.factionsprivateer.user.factory.PrivateerUserFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

/**
 * @author SrGutyerrez
 */
public class EnderChestInventoryCloseListener implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        Inventory inventory = event.getInventory();

        if (!inventory.getName().equals(PrivateerUser.ENDER_CHEST_TITLE)) return;

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

        privateerUser.setEnderChest(inventory);
    }
}
