package br.com.twinsflammer.factionscaribe.mcmmo.commands;

import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.util.Permissions;
import org.bukkit.command.CommandSender;

public class McgodCommand extends ToggleCommand {

    @Override
    protected boolean hasOtherPermission(CommandSender sender) {
        return Permissions.mcgodOthers(sender);
    }

    @Override
    protected boolean hasSelfPermission(CommandSender sender) {
        return Permissions.mcgod(sender);
    }

    @Override
    protected void applyCommandAction(McMMOPlayer mcMMOPlayer) {
        mcMMOPlayer.getPlayer().sendMessage(LocaleLoader.getString("Commands.GodMode." + (mcMMOPlayer.getGodMode() ? "Disabled" : "Enabled")));
        mcMMOPlayer.toggleGodMode();
    }

    @Override
    protected void sendSuccessMessage(CommandSender sender, String playerName) {
        sender.sendMessage(LocaleLoader.getString("Commands.GodMode.Toggle", playerName));
    }
}
