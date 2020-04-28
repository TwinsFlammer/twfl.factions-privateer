package com.redefocus.factionscaribe.mcmmo.datatypes.party;

import com.redefocus.factionscaribe.mcmmo.commands.party.PartySubcommandType;
import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.util.Permissions;
import com.redefocus.factionscaribe.mcmmo.util.StringUtils;
import org.bukkit.entity.Player;

public enum PartyFeature {

    CHAT,
    TELEPORT,
    ALLIANCE,
    ITEM_SHARE,
    XP_SHARE;

    public String getLocaleString() {
        return LocaleLoader.getString("Party.Feature." + StringUtils.getPrettyPartyFeatureString(this).replace(" ", ""));
    }

    public String getFeatureLockedLocaleString() {
        return LocaleLoader.getString("Ability.Generic.Template.Lock", LocaleLoader.getString("Party.Feature.Locked." + StringUtils.getPrettyPartyFeatureString(this).replace(" ", ""), Config.getInstance().getPartyFeatureUnlockLevel(this)));
    }

    public boolean hasPermission(Player player) {
        PartySubcommandType partySubCommandType;
        switch (this) {
            case CHAT:
                partySubCommandType = PartySubcommandType.CHAT;
                break;
            case TELEPORT:
                partySubCommandType = PartySubcommandType.TELEPORT;
                break;
            case ALLIANCE:
                partySubCommandType = PartySubcommandType.ALLIANCE;
                break;
            case ITEM_SHARE:
                partySubCommandType = PartySubcommandType.ITEMSHARE;
                break;
            case XP_SHARE:
                partySubCommandType = PartySubcommandType.XPSHARE;
                break;
            default:
                return false;
        }

        return Permissions.partySubcommand(player, partySubCommandType);
    }
}
