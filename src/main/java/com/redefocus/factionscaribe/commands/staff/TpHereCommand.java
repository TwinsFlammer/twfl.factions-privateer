package com.redefocus.factionscaribe.commands.staff;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import org.bukkit.command.CommandSender;

public class TpHereCommand extends CustomCommand {
    public TpHereCommand() {
        super(
                "tphere",
                CommandRestriction.IN_GAME,
                GroupNames.COORDINATOR
        );
    }

    /**
     * ESTOU ALMOÇANDO, JÁ VOLTO
     */

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage("§cUtilize /tphere <jogador>.");
            return;
        }
    }
}
