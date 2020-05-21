package br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party;

import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.ItemUtils;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.StringUtils;
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
