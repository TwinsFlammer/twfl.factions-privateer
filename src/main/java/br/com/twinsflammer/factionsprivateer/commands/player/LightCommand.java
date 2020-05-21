package br.com.twinsflammer.factionsprivateer.commands.player;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class LightCommand extends CustomCommand {
    public LightCommand() {
        super(
                "luz",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());

        privateerUser.setLight(!privateerUser.hasLight());

        commandSender.sendMessage(
                String.format(
                        "§eSua lanterna foi %s.",
                        privateerUser.hasLight() ? "ativada" : "desativada"
                )
        );
    }
}
