package br.com.twinsflammer.factionsprivateer.commands.player.enderchest.command;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author SrGutyerrez
 */
public class EnderChestCommand extends CustomCommand {
    public EnderChestCommand() {
        super(
                "enderchest",
                CommandRestriction.IN_GAME,
                GroupNames.FARMER
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] strings) {
        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());

        Player player = (Player) commandSender;

        Inventory inventory = privateerUser.getEnderChest();

        Integer size = privateerUser.getEnderChestRows() * 9;

        if (inventory.getSize() != size) {
            Inventory newInventory = Bukkit.createInventory(player, size, PrivateerUser.ENDER_CHEST_TITLE);

            for (int i = 0; i < size; i++) {
                ItemStack itemStack = inventory.getItem(i);

                newInventory.setItem(i, itemStack);
            }

            inventory = newInventory;
        }

        player.openInventory(inventory);
    }
}
