package br.com.twinsflammer.factionsprivateer.combat.listener;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
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
        
        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());
        PrivateerUser privateerUser1 = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(killer.getUniqueId());

        privateerUser.setCombatDuration(0L);
        privateerUser1.setCombatDuration(0L);
    }
}
