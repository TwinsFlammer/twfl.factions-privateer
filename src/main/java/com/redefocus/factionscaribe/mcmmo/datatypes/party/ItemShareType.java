package com.redefocus.factionscaribe.mcmmo.datatypes.party;

import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.util.ItemUtils;
import com.redefocus.factionscaribe.mcmmo.util.StringUtils;
import org.bukkit.inventory.ItemStack;

public enum ItemShareType {

    LOOT,
    MINING,
    HERBALISM,
    WOODCUTTING,
    MISC;

    public static ItemShareType getShareType(ItemStack itemStack) {
        if (ItemUtils.isMobDrop(itemStack)) {
            return LOOT;
        } else if (ItemUtils.isMiningDrop(itemStack)) {
            return MINING;
        } else if (ItemUtils.isHerbalismDrop(itemStack)) {
            return HERBALISM;
        } else if (ItemUtils.isWoodcuttingDrop(itemStack)) {
            return WOODCUTTING;
        } else if (ItemUtils.isMiscDrop(itemStack)) {
            return MISC;
        }

        return null;
    }

    public String getLocaleString() {
        return LocaleLoader.getString("Party.ItemShare.Category." + StringUtils.getCapitalized(this.toString()));
    }
}
