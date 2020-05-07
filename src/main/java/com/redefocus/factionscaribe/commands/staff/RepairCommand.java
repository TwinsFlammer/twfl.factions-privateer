package com.redefocus.factionscaribe.commands.staff;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author SrGutyerrez
 */
public class RepairCommand extends CustomCommand {
    private final Material[] ALLOWED_MATERIALS = {
            Material.DIAMOND_PICKAXE,
            Material.DIAMOND_SWORD,
            Material.DIAMOND_AXE,
            Material.DIAMOND_SPADE,
            Material.DIAMOND_HOE,
            Material.DIAMOND_BOOTS,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_HELMET,
            Material.GOLD_PICKAXE,
            Material.GOLD_SWORD,
            Material.GOLD_AXE,
            Material.GOLD_SPADE,
            Material.GOLD_HOE,
            Material.GOLD_BOOTS,
            Material.GOLD_LEGGINGS,
            Material.GOLD_CHESTPLATE,
            Material.GOLD_HELMET,
            Material.IRON_PICKAXE,
            Material.IRON_SWORD,
            Material.IRON_AXE,
            Material.IRON_SPADE,
            Material.IRON_HOE,
            Material.IRON_BOOTS,
            Material.IRON_LEGGINGS,
            Material.IRON_CHESTPLATE,
            Material.IRON_HELMET,
            Material.LEATHER_BOOTS,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_HELMET,
            Material.STONE_PICKAXE,
            Material.STONE_SWORD,
            Material.STONE_AXE,
            Material.STONE_SPADE,
            Material.STONE_HOE,
            Material.WOOD_PICKAXE,
            Material.WOOD_SWORD,
            Material.WOOD_AXE,
            Material.WOOD_SPADE,
            Material.WOOD_HOE
    };

    public RepairCommand() {
        super(
                "reparar",
                CommandRestriction.IN_GAME,
                GroupNames.DIRECTOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        Player player = (Player) commandSender;

        ItemStack itemStack = player.getItemInHand();

        if (itemStack == null || !Arrays.asList(this.ALLOWED_MATERIALS).contains(itemStack.getType())) {
            commandSender.sendMessage("§cEste item não pode ser reparado.");
            return;
        }

        itemStack.setDurability((short) 0);

        commandSender.sendMessage("§aVocê reparou o item em sua mão com sucesso.");
    }
}
