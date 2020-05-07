package com.redefocus.factionscaribe.commands.staff.inventory.command;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author SrGutyerrez
 */
public class InventoryCommand extends CustomCommand {
    public InventoryCommand() {
        super(
                "invsee",
                CommandRestriction.IN_GAME,
                GroupNames.ADMINISTRATOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        Player player = (Player) commandSender;

        if (args.length == 1) {
            String targetName = args[0];

            User user1 = UserManager.getUser(targetName);

            if (user1 == null) {
                commandSender.sendMessage("§cEste usuário não existe.");
                return;
            }

            CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user1.getId());

            if (caribeUser == null) {
                commandSender.sendMessage("§cEste usário nunca entrou neste servidor.");
                return;
            }

            Player player1 = Bukkit.getPlayerExact(targetName);

            PlayerInventory playerInventory = player1.getInventory();

            player.openInventory(playerInventory);
        } else {
            commandSender.sendMessage("§cUtilize /invsee <usuário>.");
        }
    }
}
