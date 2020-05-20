package br.com.twinsflammer.factionscaribe.combat.listener;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @author oNospher
 **/
public class PlayerDeathCombatListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());
        CaribeUser caribeUser1 = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(killer.getUniqueId());

        caribeUser.setCombatDuration(0L);
        caribeUser1.setCombatDuration(0L);
    }
}
