package br.com.twinsflammer.factionscaribe.mcmmo.commands.party;

import br.com.twinsflammer.factionscaribe.mcmmo.config.Config;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.party.ItemShareType;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.party.Party;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.party.PartyFeature;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.party.ShareMode;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.util.StringUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.commands.CommandUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyItemShareCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Party party = UserManager.getPlayer((Player) sender).getParty();

        if (party.getLevel() < Config.getInstance().getPartyFeatureUnlockLevel(PartyFeature.ITEM_SHARE)) {
            sender.sendMessage(LocaleLoader.getString("Party.Feature.Disabled.4"));
            return true;
        }

        switch (args.length) {
            case 2:
                ShareMode mode = ShareMode.getShareMode(args[1].toUpperCase());

                if (mode == null) {
                    sender.sendMessage(LocaleLoader.getString("Commands.Usage.2", "party", "itemshare", "<NONE | EQUAL | RANDOM>"));
                    return true;
                }

                handleChangingShareMode(party, mode);
                return true;

            case 3:
                boolean toggle;

                if (CommandUtils.shouldEnableToggle(args[2])) {
                    toggle = true;
                } else if (CommandUtils.shouldDisableToggle(args[2])) {
                    toggle = false;
                } else {
                    sender.sendMessage(LocaleLoader.getString("Commands.Usage.2", "party", "itemshare", "<loot | mining | herbalism | woodcutting | misc> <true | false>"));
                    return true;
                }

                try {
                    handleToggleItemShareCategory(party, ItemShareType.valueOf(args[1].toUpperCase()), toggle);
                } catch (IllegalArgumentException ex) {
                    sender.sendMessage(LocaleLoader.getString("Commands.Usage.2", "party", "itemshare", "<loot | mining | herbalism | woodcutting | misc> <true | false>"));
                }

                return true;

            default:
                sender.sendMessage(LocaleLoader.getString("Commands.Usage.2", "party", "itemshare", "<NONE | EQUAL | RANDOM>"));
                sender.sendMessage(LocaleLoader.getString("Commands.Usage.2", "party", "itemshare", "<loot | mining | herbalism | woodcutting | misc> <true | false>"));
                return true;
        }
    }

    private void handleChangingShareMode(Party party, ShareMode mode) {
        party.setItemShareMode(mode);

        String changeModeMessage = LocaleLoader.getString("Commands.Party.SetSharing", LocaleLoader.getString("Party.ShareType.Item"), LocaleLoader.getString("Party.ShareMode." + StringUtils.getCapitalized(mode.toString())));

        for (Player member : party.getOnlineMembers()) {
            member.sendMessage(changeModeMessage);
        }
    }

    private void handleToggleItemShareCategory(Party party, ItemShareType type, boolean toggle) {
        party.setSharingDrops(type, toggle);

        String toggleMessage = LocaleLoader.getString("Commands.Party.ToggleShareCategory", StringUtils.getCapitalized(type.toString()), toggle ? "enabled" : "disabled");

        for (Player member : party.getOnlineMembers()) {
            member.sendMessage(toggleMessage);
        }
    }
}
