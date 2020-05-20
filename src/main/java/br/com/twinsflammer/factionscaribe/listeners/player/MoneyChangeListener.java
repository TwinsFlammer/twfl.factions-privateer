package br.com.twinsflammer.factionscaribe.listeners.player;

import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import br.com.twinsflammer.factionscaribe.economy.event.MoneyChangeEvent;
import br.com.twinsflammer.factionscaribe.economy.manager.EconomyManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author SrGutyerrez
 */
public class MoneyChangeListener implements Listener {
    @EventHandler
    public void onChange(MoneyChangeEvent event) {
        CaribeUser caribeUser = event.getCaribeUser();

        caribeUser.updateScoreboard(3, EconomyManager.format(event.getNewAmount()));
    }
}
