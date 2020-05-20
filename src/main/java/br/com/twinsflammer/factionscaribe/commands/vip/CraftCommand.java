package br.com.twinsflammer.factionscaribe.commands.vip;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SrGutyerrez
 */
public class CraftCommand extends CustomCommand {
    public CraftCommand() {
        super(
                "craft",
                CommandRestriction.IN_GAME,
                GroupNames.FARMER
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        Player player = (Player) commandSender;

        player.openWorkbench(
                player.getLocation(),
                true
        );
    }
}
