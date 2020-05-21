package br.com.twinsflammer.factionsprivateer.commands.staff;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SrGutyerrez
 */
public class FlyCommand extends CustomCommand {
    public FlyCommand() {
        super(
                "fly",
                CommandRestriction.IN_GAME,
                GroupNames.DIRECTOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        Player player = (Player) commandSender;

        player.setAllowFlight(!player.getAllowFlight());
        player.setFlying(!player.isFlying());

        commandSender.sendMessage(
                String.format(
                        "§aO seu modo voo foi %s.",
                        player.isFlying() ? "ativado" : "desativado"
                )
        );
    }
}
