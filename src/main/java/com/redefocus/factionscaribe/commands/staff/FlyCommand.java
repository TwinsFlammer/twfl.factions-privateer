package com.redefocus.factionscaribe.commands.staff;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
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

        player.setFlying(!player.isFlying());
        player.setAllowFlight(!player.getAllowFlight());

        commandSender.sendMessage(
                String.format(
                        "§aO seu modo voo foi %s.",
                        player.isFlying() ? "ativado" : "desativado"
                )
        );
    }
}
