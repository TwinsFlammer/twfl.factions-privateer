package br.com.twinsflammer.factionscaribe.commands.vip;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
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
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());

        CaribeUser.Back back = caribeUser.getBack();

        if (back != null) {
            TeleportRequest teleportRequest = new TeleportRequest(
                    caribeUser.getId(),
                    null,
                    back.getLocation(),
                    back.getServerId(),
                    caribeUser.getTeleportTime()
            );

            teleportRequest.start();

            commandSender.sendMessage("§eVoltando a sua localização anterior...");
        } else {
            commandSender.sendMessage("§cVocê não tem localização para voltar.");
        }
    }
}
