package com.redefocus.factionscaribe.commands.staff;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.util.Helper;
import com.redefocus.factionscaribe.util.Enchantments;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author SrGutyerrez
 */
public class EnchantCommand extends CustomCommand {
    public EnchantCommand() {
        super(
                "enchant",
                CommandRestriction.IN_GAME,
                GroupNames.DIRECTOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        Player player = (Player) commandSender;

        switch (args.length){
            case 2: {
                ItemStack itemStack = player.getItemInHand();

                if (itemStack == null || itemStack.getType() == Material.AIR){
                    commandSender.sendMessage("§cVocê deve estar segurando um item.");
                    return;
                }

                Enchantment enchantment = Enchantments.getByName(args[0].toLowerCase());

                if (enchantment == null){
                    commandSender.sendMessage("§cEste encantamento não existe.");
                    return;
                }

                if (Helper.isInteger(args[1])){
                    Integer level = Integer.parseInt(args[1]);
                    if (level < 1){
                        commandSender.sendMessage("§cVocê inseriu um level inválido.");
                        return;
                    }

                    itemStack.addUnsafeEnchantment(enchantment, level);
                    commandSender.sendMessage("§eItem encantado com sucesso!");
                    return;
                } else {
                    commandSender.sendMessage("§cVocê inseriu um level inválido.");
                    return;
                }
            }
            default: {
                commandSender.sendMessage("§cUtilize /enchant <encantamento> <level>.");
                return;
            }
        }
    }
}
