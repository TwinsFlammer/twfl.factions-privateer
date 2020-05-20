package br.com.twinsflammer.factionscaribe.commands.vip;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author SrGutyerrez
 */
public class HatCommand extends CustomCommand {
    private final String[] ALLOWED_NAMES = {
            "_HELMET",
            "_BLOCK",
            "_SPAWNER",
            "BANNER",
            "TNT"
    };

    public HatCommand() {
        super(
                "hat",
                CommandRestriction.IN_GAME,
                GroupNames.FARMER
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        Player player = (Player) commandSender;

        ItemStack itemStack = player.getItemInHand();

        if (itemStack == null || itemStack.getType() == Material.AIR) {
            commandSender.sendMessage("§cVocê deve estar segurando um item válido.");
            return;
        }

        Material material = itemStack.getType();

        if (!this.isValid(material)) {
            PlayerInventory playerInventory = player.getInventory();

            playerInventory.setHelmet(itemStack);

            player.setItemInHand(null);

            player.updateInventory();

            player.sendMessage("§eVocê agora está usando um chapéu!");
            return;
        } else {
            commandSender.sendMessage("§cEste item não pode ser usado como um chapéu!");
            return;
        }
    }

    private Boolean isValid(Material material){
        for (String materialName : this.ALLOWED_NAMES) {
            if (material.name().contains(materialName)) return true;
        }

        return false;
    }
}
