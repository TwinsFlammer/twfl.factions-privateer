package br.com.twinsflammer.factionsprivateer.mcmmo.commands.party;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.Party;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.party.McMMOPartyChangeEvent.EventReason;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.party.PartyManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyDisbandCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                Party playerParty = UserManager.getPlayer((Player) sender).getParty();
                String partyName = playerParty.getName();

                for (Player member : playerParty.getOnlineMembers()) {
                    if (!PartyManager.handlePartyChangeEvent(member, partyName, null, EventReason.KICKED_FROM_PARTY)) {
                        return true;
                    }

                    member.sendMessage(LocaleLoader.getString("Party.Disband"));
                }

                PartyManager.disbandParty(playerParty);
                return true;

            default:
                sender.sendMessage(LocaleLoader.getString("Commands.Usage.1", "party", "disband"));
                return true;
        }
    }
}
