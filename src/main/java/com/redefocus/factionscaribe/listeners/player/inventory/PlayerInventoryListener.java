package com.redefocus.factionscaribe.listeners.player.inventory;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.manager.PacketManager;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import com.redefocus.factionscaribe.user.item.event.PlayerItemReceiveEvent;
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

import java.lang.reflect.InvocationTargetException;

/**
 * @author SrGutyerrez
 */
public class PlayerInventoryListener implements Listener {
    public PlayerInventoryListener() {
        PacketManager.registerPackets(
                this.getPacketAdapter()
        );
    }

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

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

        Inventory inventory = caribeUser.getInventory();

        if (inventory != null)
            playerInventory.setContents(inventory.getContents());

        ItemStack[] armorContents = caribeUser.getArmorContents();

        if (armorContents != null)
            playerInventory.setArmorContents(armorContents);
    }

    protected PacketAdapter getPacketAdapter() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        return new PacketAdapter(
                FactionsCaribe.getInstance(),
                PacketType.Play.Server.WINDOW_ITEMS
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packetContainer = protocolManager.createPacket(PacketType.Play.Server.TAB_COMPLETE);
                Player player = event.getPlayer();

                System.out.println("packet legal aeuaueaueuaueuea");

                try {
                    protocolManager.sendServerPacket(player, packetContainer);
                } catch (InvocationTargetException exception) {
                    exception.printStackTrace();
                }
            }
        };
    }

    protected void updateInventory(Player player) {
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

        Inventory inventory = player.getInventory();
        PlayerInventory playerInventory = player.getInventory();

        caribeUser.setInventory(inventory, playerInventory.getArmorContents());
    }
}
