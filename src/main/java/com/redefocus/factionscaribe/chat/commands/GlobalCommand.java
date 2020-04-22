package com.redefocus.factionscaribe.chat.commands;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.cooldown.manager.CooldownManager;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.util.TimeFormatter;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class GlobalCommand extends CustomCommand {
    public GlobalCommand() {
        super(
                "g",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (CooldownManager.inCooldown(user, "CHAT_GLOBAL")) {
            commandSender.sendMessage(
                    String.format(
                            "",
                            TimeFormatter.formatMinimized(CooldownManager.getRemainingTime(user, "CHAT_GLOBAL"))
                    )
            );
            return;
        }
    }
}
