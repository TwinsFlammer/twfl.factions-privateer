package com.redefocus.factionscaribe.mcmmo.commands.party.teleport;

import com.redefocus.factionscaribe.mcmmo.datatypes.party.PartyTeleportRecord;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.util.Permissions;
import com.redefocus.factionscaribe.mcmmo.util.player.UserManager;
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
