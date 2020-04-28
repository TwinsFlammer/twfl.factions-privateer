package com.redefocus.factionscaribe.mcmmo.commands.party.alliance;

import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.party.PartyManager;
import com.redefocus.factionscaribe.mcmmo.util.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyAllianceAcceptCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 2:
                Player player = (Player) sender;
                McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);

                if (!mcMMOPlayer.hasPartyAllianceInvite()) {
                    sender.sendMessage(LocaleLoader.getString("mcMMO.NoInvites"));
                    return true;
                }

                if (mcMMOPlayer.getParty().getAlly() != null) {
                    player.sendMessage(LocaleLoader.getString("Commands.Party.Alliance.AlreadyAllies"));
                    return true;
                }

                PartyManager.acceptAllianceInvite(mcMMOPlayer);
                return true;

            default:
                sender.sendMessage(LocaleLoader.getString("Commands.Usage.2", "party", "alliance", "accept"));
                return true;
        }
    }
}
