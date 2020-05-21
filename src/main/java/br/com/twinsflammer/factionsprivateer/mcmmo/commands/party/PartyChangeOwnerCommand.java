package br.com.twinsflammer.factionsprivateer.mcmmo.commands.party;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.Party;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.party.PartyManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.commands.CommandUtils;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyChangeOwnerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 2:
                Party playerParty = UserManager.getPlayer((Player) sender).getParty();
                String targetName = CommandUtils.getMatchedPlayerName(args[1]);
                OfflinePlayer target = FactionsPrivateer.getInstance().getServer().getOfflinePlayer(targetName);

                if (!playerParty.hasMember(target.getUniqueId())) {
                    sender.sendMessage(LocaleLoader.getString("Party.NotInYourParty", targetName));
                    return true;
                }

                PartyManager.setPartyLeader(target.getUniqueId(), playerParty);
                return true;

            default:
                sender.sendMessage(LocaleLoader.getString("Commands.Usage.2", "party", "owner", "<" + LocaleLoader.getString("Commands.Usage.Player") + ">"));
                return true;
        }
    }
}
