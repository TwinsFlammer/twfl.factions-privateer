package br.com.twinsflammer.factionscaribe.commands.staff;

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
public class GodCommand extends CustomCommand {
    public GodCommand() {
        super(
                "god",
                CommandRestriction.IN_GAME,
                GroupNames.MODERATOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());

        caribeUser.setGod(!caribeUser.isGod());

        commandSender.sendMessage(
                String.format(
                        "§aO modo Deus foi %s.",
                        caribeUser.isGod() ? "ativado" : "desativado"
                )
        );
    }
}
