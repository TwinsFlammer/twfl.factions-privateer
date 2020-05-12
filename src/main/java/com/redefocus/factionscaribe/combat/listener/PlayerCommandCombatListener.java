package com.redefocus.factionscaribe.combat.listener;

import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;

/**
 * @author oNospher
 **/
public class PlayerCommandCombatListener {
    private final String[] ALLOWED_COMMANDS = {
            "/g",
            "/c",
            "/.",
            "/tell",
            "/a",
            "/report"
    };

    protected final String NOT_ALLOWED_COMMAND_IN_COMBAT = "§cO uso de comandos em combate está desabilitado.";

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.contains(" "))
            message = message.split(" ")[0];

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

        if (caribeUser.inCombat()) {
            if (Arrays.asList(ALLOWED_COMMANDS).contains(message.toLowerCase())) return;

            event.setCancelled(true);
            player.sendMessage(this.NOT_ALLOWED_COMMAND_IN_COMBAT);
        }
    }
}
