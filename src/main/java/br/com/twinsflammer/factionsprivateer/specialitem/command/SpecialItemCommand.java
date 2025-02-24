package br.com.twinsflammer.factionsprivateer.specialitem.command;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.specialitem.data.AbstractSpecialItem;
import br.com.twinsflammer.factionsprivateer.specialitem.manager.SpecialItemManager;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author SrGutyerrez
 */
public class SpecialItemCommand extends CustomCommand {
    public SpecialItemCommand() {
        super(
                "item",
                CommandRestriction.ALL,
                GroupNames.MASTER
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length != 3) {
            commandSender.sendMessage("§cUtilize /item <usuário> <item> <quantia>.");
            return;
        }

        String targetName = args[0];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            commandSender.sendMessage("§cEste usuário não existe.");
            return;
        }

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(targetName);

        if (privateerUser == null) {
            commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
            return;
        }

        if (!privateerUser.isOnlineHere()) {
            commandSender.sendMessage("§cEste usuário não está online.");
            return;
        }

        AbstractSpecialItem abstractSpecialItem = SpecialItemManager.getSpecialItem(args[1]);

        if (abstractSpecialItem == null) {
            commandSender.sendMessage("§cEste item não existe.");
            return;
        }

        Player player = privateerUser.getPlayer();

        Integer amount = Helper.isInteger(args[2]) ? Integer.parseInt(args[2]) : null;

        if (amount == null || amount <= 0) {
            commandSender.sendMessage("§cVocê inseriu uma quantia inválida.");
            return;
        }

        PlayerInventory playerInventory = player.getInventory();

        ItemStack itemStack = abstractSpecialItem.getItemStack()
                .clone();

        if ((privateerUser.getInventorySpace() * itemStack.getMaxStackSize()) < amount) {
            commandSender.sendMessage("§cEste usuário não possui espaço suficiente no inventário.");
            return;
        }

        itemStack.setAmount(amount);

        playerInventory.addItem(itemStack);

        commandSender.sendMessage(
                String.format(
                        "§aO jogador %s §arecebeu %d %s.",
                        privateerUser.getPrefix() + privateerUser.getDisplayName(),
                        amount,
                        abstractSpecialItem.getName()
                )
        );
    }
}
