package com.redefocus.factionscaribe.commands.staff;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SrGutyerrez
 */
public class VanishCommand extends CustomCommand {
    public VanishCommand() {
        super(
                "v",
                CommandRestriction.IN_GAME,
                GroupNames.MODERATOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());

        caribeUser.setInvisible(!caribeUser.isInvisible());

        commandSender.sendMessage("§aO modo invisível foi " + (caribeUser.isInvisible() ? "ativado." : "desativado."));

        Player player = (Player) commandSender;

        Bukkit.getOnlinePlayers().forEach(player1 -> {
            CaribeUser caribeUser1 = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player1.getUniqueId());

            if (caribeUser.isInvisible() && !caribeUser1.isStaff())
                player1.hidePlayer(player);
            else player1.showPlayer(player);
        });
    }
}
