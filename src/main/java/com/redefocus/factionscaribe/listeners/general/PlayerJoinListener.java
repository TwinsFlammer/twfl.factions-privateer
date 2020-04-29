package com.redefocus.factionscaribe.listeners.general;

import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author SrGutyerrez
 */
public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

        caribeUser.setupScoreboard();
    }
}
