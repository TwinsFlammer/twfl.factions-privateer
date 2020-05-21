package br.com.twinsflammer.factionsprivateer.mcmmo.commands.party;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.Party;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.PartyFeature;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.ShareMode;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.StringUtils;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.commands.CommandUtils;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyXpShareCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Party party = UserManager.getPlayer((Player) sender).getParty();

        if (party.getLevel() < Config.getInstance().getPartyFeatureUnlockLevel(PartyFeature.XP_SHARE)) {
            sender.sendMessage(LocaleLoader.getString("Party.Feature.Disabled.5"));
            return true;
        }

        switch (args.length) {
            case 2:
                if (args[1].equalsIgnoreCase("none") || CommandUtils.shouldDisableToggle(args[1])) {
                    handleChangingShareMode(party, ShareMode.NONE);
                } else if (args[1].equalsIgnoreCase("equal") || args[1].equalsIgnoreCase("even") || CommandUtils.shouldEnableToggle(args[1])) {
                    handleChangingShareMode(party, ShareMode.EQUAL);
                } else {
                    sender.sendMessage(LocaleLoader.getString("Commands.Usage.2", "party", "xpshare", "<NONE | EQUAL>"));
                }

                return true;

            default:
                sender.sendMessage(LocaleLoader.getString("Commands.Usage.2", "party", "xpshare", "<NONE | EQUAL>"));
                return true;
        }
    }

    private void handleChangingShareMode(Party party, ShareMode mode) {
        party.setXpShareMode(mode);

        String changeModeMessage = LocaleLoader.getString("Commands.Party.SetSharing", LocaleLoader.getString("Party.ShareType.Xp"), LocaleLoader.getString("Party.ShareMode." + StringUtils.getCapitalized(mode.toString())));

        for (Player member : party.getOnlineMembers()) {
            member.sendMessage(changeModeMessage);
        }
    }
}
