package br.com.twinsflammer.factionscaribe.commands.staff;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.spigot.inventory.item.CustomItem;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author SrGutyerrez
 */
public class SkullCommand extends CustomCommand {
    public SkullCommand() {
        super(
                "skull",
                CommandRestriction.IN_GAME,
                GroupNames.DIRECTOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length == 1) {
            Player player = (Player) commandSender;
            PlayerInventory playerInventory = player.getInventory();

            String owner = args[0];

            CustomItem customItem = new CustomItem(Material.SKULL_ITEM)
                    .owner(owner)
                    .name("§eCabeça de " + owner);

            playerInventory.addItem(customItem.build());
        } else {
            commandSender.sendMessage("§cUtilize /skull <nome>.");
        }
    }
}
