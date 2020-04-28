package com.redefocus.factionscaribe.mcmmo.commands.party;

import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.party.PartyManager;
import com.redefocus.factionscaribe.mcmmo.util.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCreateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 2:
            case 3:
                Player player = (Player) sender;
                McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);

                // Check to see if the party exists, and if it does cancel creating a new party
                if (PartyManager.checkPartyExistence(player, args[1])) {
                    return true;
                }

                // Changing parties
                if (!PartyManager.changeOrJoinParty(mcMMOPlayer, args[1])) {
                    return true;
                }

                PartyManager.createParty(mcMMOPlayer, args[1], getPassword(args));
                return true;

            default:
                sender.sendMessage(LocaleLoader.getString("Commands.Usage.3", "party", "create", "<" + LocaleLoader.getString("Commands.Usage.PartyName") + ">", "[" + LocaleLoader.getString("Commands.Usage.Password") + "]"));
                return true;
        }
    }

    private String getPassword(String[] args) {
        if (args.length == 3) {
            return args[2];
        }

        return null;
    }
}
