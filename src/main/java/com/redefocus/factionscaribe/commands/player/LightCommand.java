package com.redefocus.factionscaribe.commands.player;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class LightCommand extends CustomCommand {
    public LightCommand() {
        super(
                "luz",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());

        caribeUser.setLight(!caribeUser.hasLight());

        commandSender.sendMessage(
                String.format(
                        "§eSua lanterna foi %s.",
                        caribeUser.hasLight() ? "ativada" : "desativada"
                )
        );
    }
}
