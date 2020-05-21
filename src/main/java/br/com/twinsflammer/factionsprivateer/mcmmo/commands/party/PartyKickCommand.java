package br.com.twinsflammer.factionsprivateer.mcmmo.commands.party;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.Party;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.party.McMMOPartyChangeEvent.EventReason;
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

public class PartyKickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 2:
                Party playerParty = UserManager.getPlayer((Player) sender).getParty();
                String targetName = CommandUtils.getMatchedPlayerName(args[1]);

                if (!playerParty.hasMember(targetName)) {
                    sender.sendMessage(LocaleLoader.getString("Party.NotInYourParty", targetName));
                    return true;
                }

                OfflinePlayer target = FactionsPrivateer.getInstance().getServer().getOfflinePlayer(targetName);

                if (target.isOnline()) {
                    Player onlineTarget = target.getPlayer();
                    String partyName = playerParty.getName();

                    if (!PartyManager.handlePartyChangeEvent(onlineTarget, partyName, null, EventReason.KICKED_FROM_PARTY)) {
                        return true;
                    }

                    PartyManager.processPartyLeaving(UserManager.getPlayer(onlineTarget));
                    onlineTarget.sendMessage(LocaleLoader.getString("Commands.Party.Kick", partyName));
                }

                PartyManager.removeFromParty(target, playerParty);
                return true;

            default:
                sender.sendMessage(LocaleLoader.getString("Commands.Usage.2", "party", "kick", "<" + LocaleLoader.getString("Commands.Usage.Player") + ">"));
                return true;
        }
    }
}
