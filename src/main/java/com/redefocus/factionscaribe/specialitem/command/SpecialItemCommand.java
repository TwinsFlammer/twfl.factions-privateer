package com.redefocus.factionscaribe.specialitem.command;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.common.shared.util.Helper;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.specialitem.data.AbstractSpecialItem;
import com.redefocus.factionscaribe.specialitem.manager.SpecialItemManager;
import com.redefocus.factionscaribe.user.data.CaribeUser;
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

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(targetName);

        if (caribeUser == null) {
            commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
            return;
        }

        if (!caribeUser.isOnlineHere()) {
            commandSender.sendMessage("§cEste usuário não está online.");
            return;
        }

        AbstractSpecialItem abstractSpecialItem = SpecialItemManager.getSpecialItem(args[1]);

        if (abstractSpecialItem == null) {
            commandSender.sendMessage("§cEste item não existe.");
            return;
        }

        Player player = caribeUser.getPlayer();

        Integer amount = Helper.isInteger(args[2]) ? Integer.parseInt(args[2]) : null;

        if (amount == null || amount <= 0) {
            commandSender.sendMessage("§cVocê inseriu uma quantia inválida.");
            return;
        }

        PlayerInventory playerInventory = player.getInventory();

        ItemStack itemStack = abstractSpecialItem.getItemStack()
                .clone();

        if ((caribeUser.getInventorySpace() * itemStack.getMaxStackSize()) < amount) {
            commandSender.sendMessage("§cEste usuário não possui espaço suficiente no inventário.");
            return;
        }

        itemStack.setAmount(amount);

        playerInventory.addItem(itemStack);

        commandSender.sendMessage(
                String.format(
                        "§aO jogador %s §arecebeu %d %s.",
                        caribeUser.getPrefix() + caribeUser.getDisplayName(),
                        amount,
                        abstractSpecialItem.getName()
                )
        );
    }
}
