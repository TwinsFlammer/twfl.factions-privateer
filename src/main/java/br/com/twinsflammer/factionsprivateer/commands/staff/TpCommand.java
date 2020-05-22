package br.com.twinsflammer.factionsprivateer.commands.staff;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.spigot.teleport.data.TeleportRequest;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
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

        PrivateerUser targetUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(targetName);

        if (targetUser == null) {
            commandSender.sendMessage("§cEste usuário não existe.");
            return;
        }

        if (!targetUser.isOnline()) {
            commandSender.sendMessage("§cEste usuário não está online.");
            return;
        }

        if(!user.hasGroup(GroupNames.COORDINATOR) && user.getReports().isEmpty()) {
            commandSender.sendMessage("§cEste usuário não foi reportado nenhuma vez.");
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
