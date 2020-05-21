package br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party;

import br.com.twinsflammer.factionsprivateer.mcmmo.commands.party.PartySubcommandType;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Permissions;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.StringUtils;
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
