package br.com.twinsflammer.factionsprivateer.listeners.player;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author SrGutyerrez
 */
public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

        if (privateerUser.getServer() != null) return;

        privateerUser.removeInventory();

        PlayerInventory playerInventory = player.getInventory();

        playerInventory.clear();
        playerInventory.setArmorContents(null);
    }
}
