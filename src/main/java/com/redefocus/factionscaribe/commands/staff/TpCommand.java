package com.redefocus.factionscaribe.commands.staff;

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
public class TpCommand extends CustomCommand {
    public TpCommand() {
        super(
                "tp",
                CommandRestriction.IN_GAME,
                GroupNames.MODERATOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage("§cUtilize /tp <jogador>.");
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

        Server server = targetUser.getServer();

        TeleportRequest teleportRequest = new TeleportRequest(
                user.getId(),
                targetUser.getId(),
                null,
                server.getId(),
                0L
        );

        teleportRequest.start();

        commandSender.sendMessage(
                String.format(
                        "§aTeleportando-se para %s§a.",
                        targetUser.getPrefix() + targetUser.getDisplayName()
                )
        );
    }
}
