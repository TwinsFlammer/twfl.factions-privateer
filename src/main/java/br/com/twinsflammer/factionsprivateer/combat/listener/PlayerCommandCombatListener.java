package br.com.twinsflammer.factionsprivateer.combat.listener;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;

/**
 * @author oNospher
 **/
public class PlayerCommandCombatListener implements Listener {
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

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

        if (privateerUser.inCombat()) {
            if (Arrays.asList(ALLOWED_COMMANDS).contains(message.toLowerCase())) return;

            event.setCancelled(true);
            player.sendMessage(this.NOT_ALLOWED_COMMAND_IN_COMBAT);
        }
    }
}
