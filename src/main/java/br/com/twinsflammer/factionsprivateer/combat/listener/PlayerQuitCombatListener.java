package br.com.twinsflammer.factionsprivateer.combat.listener;

import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.stream.Stream;

/**
 * @author oNospher
 **/
public class PlayerQuitCombatListener implements Listener {
    private final String[] DISCONNECT_MESSAGES = {
            "que vergonha...",
            "que feio...",
            "fraco..."
    };

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(SpigotAPI.getCurrentServer().isRestarting()) return;

        Player player = event.getPlayer();

        this.finishCombat(player);
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if(SpigotAPI.getCurrentServer().isRestarting()) return;

        Player player = event.getPlayer();

        this.finishCombat(player);
    }

    void finishCombat(Player player) {
        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

        if(privateerUser.inCombat()) {
            player.setHealth(0.0D);

            privateerUser.setCombatDuration(0L);

            SpigotAPI.getUsers()
                    .forEach(user -> {
                        user.sendMessage(
                                String.format(
                                        "§c%s §cdesconectou em combate, %s",
                                        privateerUser.getPrefix() + privateerUser.getDisplayName(),
                                        Stream.of(this.DISCONNECT_MESSAGES)
                                                .findAny()
                                                .orElseGet(() -> "coitado...")
                                )
                        );
                    });
        }
    }
}
