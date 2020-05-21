package br.com.twinsflammer.factionsprivateer.listeners.player;

import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.factionsprivateer.economy.event.MoneyChangeEvent;
import br.com.twinsflammer.factionsprivateer.economy.manager.EconomyManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author SrGutyerrez
 */
public class MoneyChangeListener implements Listener {
    @EventHandler
    public void onChange(MoneyChangeEvent event) {
        PrivateerUser privateerUser = event.getPrivateerUser();

        privateerUser.updateScoreboard(3, EconomyManager.format(event.getNewAmount()));
    }
}
