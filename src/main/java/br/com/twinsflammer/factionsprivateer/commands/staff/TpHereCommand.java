package br.com.twinsflammer.factionsprivateer.commands.staff;

import br.com.twinsflammer.api.spigot.SpigotAPI;
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

        PrivateerUser targetUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(targetName);

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
