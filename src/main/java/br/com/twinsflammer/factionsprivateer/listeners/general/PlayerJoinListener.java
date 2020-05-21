package br.com.twinsflammer.factionsprivateer.listeners.general;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
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

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

        privateerUser.setupScoreboard();

        if (!privateerUser.isStaff())
            FactionsPrivateer.getInstance().getCaribeUsers()
                    .stream()
                    .filter(PrivateerUser::isInvisible)
                    .forEach(caribeUser1 -> {
                        Player player1 = caribeUser1.getPlayer();

                        if (player1 != null) {
                            player.hidePlayer(player1);
                        }
                    });
    }
}
