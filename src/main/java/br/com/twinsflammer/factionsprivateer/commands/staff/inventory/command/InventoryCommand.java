package br.com.twinsflammer.factionsprivateer.commands.staff.inventory.command;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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

            PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getUniqueId());
            PrivateerUser privateerUser1 = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user1.getId());

            if (privateerUser1 == null) {
                commandSender.sendMessage("§cEste usário nunca entrou neste servidor.");
                return;
            }

            Inventory playerInventory = privateerUser1.getInventory();

            player.openInventory(playerInventory);

            privateerUser.setSeeingInventory(playerInventory);
        } else {
            commandSender.sendMessage("§cUtilize /invsee <usuário>.");
        }
    }
}
