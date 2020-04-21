package com.redefocus.factionscaribe.commands.staff;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.api.spigot.teleport.data.TeleportRequest;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.server.data.Server;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class TpHereCommand extends CustomCommand {
    public TpHereCommand() {
        super(
                "tphere",
                CommandRestriction.IN_GAME,
                GroupNames.COORDINATOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage("§cUtilize /tphere <jogador>.");
            return;
        }

        String targetName = args[0];

        CaribeUser targetUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(targetName);

        if (targetUser == null) {
            commandSender.sendMessage("§cEste usuário não existe.");
            return;
        }

        if (!targetUser.isOnline()) {
            commandSender.sendMessage("§cEste usuário não está online.");
            return;
        }

        Server server = SpigotAPI.getCurrentServer();

        TeleportRequest teleportRequest = new TeleportRequest(
                targetUser.getId(),
                user.getId(),
                null,
                server.getId(),
                0L
        );

        teleportRequest.start();

        commandSender.sendMessage(
                String.format(
                        "§aTeleportando %s§a até você.",
                        targetUser.getPrefix() + targetUser.getDisplayName()
                )
        );
    }
}
