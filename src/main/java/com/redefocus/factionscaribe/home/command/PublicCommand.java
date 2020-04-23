package com.redefocus.factionscaribe.home.command;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import org.bukkit.command.CommandSender;

/**
 * @author oNospher
 **/
public class PublicCommand extends CustomCommand {

    public PublicCommand() {
        super(
                "publica",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] strings) {

    }
}
