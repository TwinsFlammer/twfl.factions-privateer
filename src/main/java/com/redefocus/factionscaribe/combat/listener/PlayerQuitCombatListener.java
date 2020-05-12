package com.redefocus.factionscaribe.combat.listener;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.stream.Stream;

/**
 * @author oNospher
 **/
public class PlayerQuitCombatListener {
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
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

        if(caribeUser.inCombat()) {
            player.setHealth(0.0D);

            caribeUser.setCombatDuration(0L);

            SpigotAPI.getUsers()
                    .forEach(user -> {
                        user.sendMessage(
                                String.format(
                                        "§c%s §cdesconectou em combate, %s",
                                        caribeUser.getPrefix() + caribeUser.getDisplayName(),
                                        Stream.of(this.DISCONNECT_MESSAGES)
                                                .findAny()
                                                .orElseGet(() -> "coitado...")
                                )
                        );
                    });
        }
    }
}
