package br.com.twinsflammer.factionscaribe.commands.player;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
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
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());

        caribeUser.setLight(!caribeUser.hasLight());

        commandSender.sendMessage(
                String.format(
                        "§eSua lanterna foi %s.",
                        caribeUser.hasLight() ? "ativada" : "desativada"
                )
        );
    }
}
