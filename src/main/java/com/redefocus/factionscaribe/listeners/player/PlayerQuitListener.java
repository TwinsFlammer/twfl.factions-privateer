package com.redefocus.factionscaribe.listeners.player;

import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author SrGutyerrez
 */
public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

        FactionsCaribe.unloadUser(caribeUser);
    }
}
