package com.redefocus.factionscaribe.listeners.player;

import com.redefocus.factionscaribe.economy.event.MoneyChangeEvent;
import com.redefocus.factionscaribe.economy.manager.EconomyManager;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author SrGutyerrez
 */
public class MoneyChangeListener implements Listener {
    @EventHandler
    public void onChange(MoneyChangeEvent event) {
        CaribeUser caribeUser = event.getCaribeUser();

        caribeUser.updateScoreboard(3, EconomyManager.format(caribeUser.getMoney()));
    }
}
