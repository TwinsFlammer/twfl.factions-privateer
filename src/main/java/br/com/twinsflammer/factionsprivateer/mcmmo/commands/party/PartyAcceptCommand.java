package br.com.twinsflammer.factionsprivateer.mcmmo.commands.party;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.party.PartyManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyAcceptCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                Player player = (Player) sender;
                McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);

                if (!mcMMOPlayer.hasPartyInvite()) {
                    sender.sendMessage(LocaleLoader.getString("mcMMO.NoInvites"));
                    return true;
                }

                // Changing parties
                if (!PartyManager.changeOrJoinParty(mcMMOPlayer, mcMMOPlayer.getPartyInvite().getName())) {
                    return true;
                }

                PartyManager.joinInvitedParty(mcMMOPlayer);
                return true;

            default:
                sender.sendMessage(LocaleLoader.getString("Commands.Usage.1", "party", "accept"));
                return true;
        }
    }
}
