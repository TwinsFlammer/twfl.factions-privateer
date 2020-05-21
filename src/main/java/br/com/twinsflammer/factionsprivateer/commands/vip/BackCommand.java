package br.com.twinsflammer.factionsprivateer.commands.vip;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.spigot.teleport.data.TeleportRequest;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class BackCommand extends CustomCommand {
    public BackCommand() {
        super(
                "back",
                CommandRestriction.IN_GAME,
                GroupNames.FARMER
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] strings) {
        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());

        PrivateerUser.Back back = privateerUser.getBack();

        if (back != null) {
            TeleportRequest teleportRequest = new TeleportRequest(
                    privateerUser.getId(),
                    null,
                    back.getLocation(),
                    back.getServerId(),
                    privateerUser.getTeleportTime()
            );

            teleportRequest.start();

            commandSender.sendMessage("§eVoltando a sua localização anterior...");
        } else {
            commandSender.sendMessage("§cVocê não tem localização para voltar.");
        }
    }
}
