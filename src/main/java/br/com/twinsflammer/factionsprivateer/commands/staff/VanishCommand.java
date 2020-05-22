package br.com.twinsflammer.factionsprivateer.commands.staff;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SrGutyerrez
 */
public class VanishCommand extends CustomCommand {
    public VanishCommand() {
        super(
                "v",
                CommandRestriction.IN_GAME,
                GroupNames.MODERATOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());

        privateerUser.setInvisible(!privateerUser.isInvisible());

        commandSender.sendMessage("§aO modo invisível foi " + (privateerUser.isInvisible() ? "ativado." : "desativado."));

        privateerUser.toggleVisibility();
    }
}
