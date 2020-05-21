package br.com.twinsflammer.factionsprivateer.mcmmo.commands.party.teleport;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.PartyTeleportRecord;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Permissions;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PtpToggleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Permissions.partyTeleportToggle(sender)) {
            sender.sendMessage(command.getPermissionMessage());
            return true;
        }

        PartyTeleportRecord ptpRecord = UserManager.getPlayer(sender.getName()).getPartyTeleportRecord();

        if (ptpRecord.isEnabled()) {
            sender.sendMessage(LocaleLoader.getString("Commands.ptp.Disabled"));
        } else {
            sender.sendMessage(LocaleLoader.getString("Commands.ptp.Enabled"));
        }

        ptpRecord.toggleEnabled();
        return true;
    }
}
