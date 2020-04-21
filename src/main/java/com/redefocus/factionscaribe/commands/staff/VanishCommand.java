package com.redefocus.factionscaribe.commands.staff;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

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
        CaribeUser caribeUser = (CaribeUser) user;

        caribeUser.setInvisible(!caribeUser.isInvisible());

        commandSender.sendMessage("§aO modo invisível foi " + (caribeUser.isInvisible() ? "ativado." : "desativado."));
    }
}
