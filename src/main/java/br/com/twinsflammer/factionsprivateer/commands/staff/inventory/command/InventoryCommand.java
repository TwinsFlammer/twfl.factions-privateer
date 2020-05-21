package br.com.twinsflammer.factionsprivateer.commands.staff.inventory.command;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
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

            PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user1.getId());

            if (privateerUser == null) {
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
