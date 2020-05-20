package br.com.twinsflammer.factionscaribe.mcmmo.util.skills;

import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SkillType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class McMMOUtils {

    public static ItemStack getSkillItemStack(SkillType skillType) {
        switch (skillType) {
            case SWORDS:
                return new ItemStack(Material.DIAMOND_SWORD);
            case ARCHERY:
                return new ItemStack(Material.BOW);
            case MINING:
                return new ItemStack(Material.DIAMOND_PICKAXE);
            case EXCAVATION:
                return new ItemStack(Material.DIAMOND_SPADE);
            case REPAIR:
                return new ItemStack(Material.ANVIL);
            case ACROBATICS:
                return new ItemStack(Material.DIAMOND_BOOTS);
            case ALCHEMY:
                return new ItemStack(Material.POTION, 1, (short) 8193);
            case AXES:
                return new ItemStack(Material.DIAMOND_AXE);
            case FISHING:
                return new ItemStack(Material.FISHING_ROD);
            case HERBALISM:
                return new ItemStack(Material.SEEDS);
            case TAMING:
                return new ItemStack(Material.NAME_TAG);
            case UNARMED:
                return new ItemStack(Material.RAW_BEEF);
            case WOODCUTTING:
                return new ItemStack(Material.IRON_AXE);
            case SALVAGE:
                return new ItemStack(Material.GOLD_HELMET);
            case SMELTING:
                return new ItemStack(Material.BLAZE_POWDER);
            default:
                return new ItemStack(Material.AIR);
        }
    }

}
