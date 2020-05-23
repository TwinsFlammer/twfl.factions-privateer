package br.com.twinsflammer.factionsprivateer.combat.listener;

import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.util.serialize.LocationSerialize;
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

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());
        privateerUser.setCombatDuration(0L);

        PrivateerUser.Back back = new PrivateerUser.Back(
                SpigotAPI.getCurrentServer().getId(),
                LocationSerialize.toString(player.getLocation())
        );

        privateerUser.setBack(back);

        Player killer = player.getKiller();

        if (killer != null) {
            PrivateerUser privateerUser1 = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(killer.getUniqueId());
            privateerUser1.setCombatDuration(0L);
        }
    }
}
