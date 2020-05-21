package br.com.twinsflammer.factionsprivateer.mcmmo.commands.party;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.Party;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.party.McMMOPartyChangeEvent.EventReason;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.party.PartyManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyQuitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                Player player = (Player) sender;
                McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);
                Party playerParty = mcMMOPlayer.getParty();

                if (!PartyManager.handlePartyChangeEvent(player, playerParty.getName(), null, EventReason.LEFT_PARTY)) {
                    return true;
                }

                PartyManager.removeFromParty(mcMMOPlayer);
                sender.sendMessage(LocaleLoader.getString("Commands.Party.Leave"));
                return true;

            default:
                sender.sendMessage(LocaleLoader.getString("Commands.Usage.1", "party", "quit"));
                return true;
        }
    }
}
