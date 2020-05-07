package com.redefocus.factionscaribe.commands.vip;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.api.spigot.teleport.data.TeleportRequest;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
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
