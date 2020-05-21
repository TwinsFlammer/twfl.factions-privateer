package br.com.twinsflammer.factionsprivateer.listeners.player.inventory;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.factionsprivateer.user.item.event.PlayerItemReceiveEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author SrGutyerrez
 */
public class PlayerInventoryListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER) return;

        ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        this.updateInventory(player);
    }

    @EventHandler
    public void onReceive(PlayerItemReceiveEvent event) {
        Player player = event.getPlayer();

        this.updateInventory(player);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        this.updateInventory(player);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        this.updateInventory(player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerInventory playerInventory = player.getInventory();

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

        Inventory inventory = privateerUser.getInventory();

        if (inventory != null)
            playerInventory.setContents(inventory.getContents());

        ItemStack[] armorContents = privateerUser.getArmorContents();

        if (armorContents != null)
            playerInventory.setArmorContents(armorContents);
    }

    protected void updateInventory(Player player) {
        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

        Inventory inventory = player.getInventory();
        PlayerInventory playerInventory = player.getInventory();

        privateerUser.setInventory(inventory, playerInventory.getArmorContents());
    }
}
