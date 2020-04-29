package com.redefocus.factionscaribe.combat.listener;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author oNospher
 **/
public class PlayerQuitCombatListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(SpigotAPI.getCurrentServer().isRestarting()) return;
        Player player = event.getPlayer();
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());
        if(caribeUser.inCombat()) {
            player.setHealth(0.0D);
            caribeUser.setCombatDuration(0L);
            Bukkit.broadcastMessage(caribeUser.getPrefix() + player.getName() + " desconectou em combate, que vergonha..");
        }
    }
}
