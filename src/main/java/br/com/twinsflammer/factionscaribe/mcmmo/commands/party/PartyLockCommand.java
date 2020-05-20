package br.com.twinsflammer.factionscaribe.mcmmo.commands.party;

import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.party.Party;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.util.Permissions;
import br.com.twinsflammer.factionscaribe.mcmmo.util.commands.CommandUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyLockCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("lock")) {
                    togglePartyLock(sender, true);
                } else if (args[0].equalsIgnoreCase("unlock")) {
                    togglePartyLock(sender, false);
                }

                return true;

            case 2:
                if (!args[0].equalsIgnoreCase("lock")) {
                    sendUsageStrings(sender);
                    return true;
                }

                if (CommandUtils.shouldEnableToggle(args[1])) {
                    togglePartyLock(sender, true);
                } else if (CommandUtils.shouldDisableToggle(args[1])) {
                    togglePartyLock(sender, false);
                } else {
                    sendUsageStrings(sender);
                }

                return true;

            default:
                sendUsageStrings(sender);
                return true;
        }
    }

    private void sendUsageStrings(CommandSender sender) {
        sender.sendMessage(LocaleLoader.getString("Commands.Usage.2", "party", "lock", "[on|off]"));
        sender.sendMessage(LocaleLoader.getString("Commands.Usage.1", "party", "unlock"));
    }

    private void togglePartyLock(CommandSender sender, boolean lock) {
        Party party = UserManager.getPlayer((Player) sender).getParty();

        if (!Permissions.partySubcommand(sender, lock ? PartySubcommandType.LOCK : PartySubcommandType.UNLOCK)) {
            sender.sendMessage(LocaleLoader.getString("mcMMO.NoPermission"));
            return;
        }

        if (lock ? party.isLocked() : !party.isLocked()) {
            sender.sendMessage(LocaleLoader.getString("Party." + (lock ? "IsLocked" : "IsntLocked")));
            return;
        }

        party.setLocked(lock);
        sender.sendMessage(LocaleLoader.getString("Party." + (lock ? "Locked" : "Unlocked")));
    }
}
