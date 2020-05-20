package br.com.twinsflammer.factionscaribe.mcmmo.commands;

import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.util.Permissions;
import org.bukkit.command.CommandSender;

public class McrefreshCommand extends ToggleCommand {

    @Override
    protected boolean hasOtherPermission(CommandSender sender) {
        return Permissions.mcrefreshOthers(sender);
    }

    @Override
    protected boolean hasSelfPermission(CommandSender sender) {
        return Permissions.mcrefresh(sender);
    }

    @Override
    protected void applyCommandAction(McMMOPlayer mcMMOPlayer) {
        mcMMOPlayer.setRecentlyHurt(0);
        mcMMOPlayer.resetCooldowns();
        mcMMOPlayer.resetToolPrepMode();
        mcMMOPlayer.resetAbilityMode();

        mcMMOPlayer.getPlayer().sendMessage(LocaleLoader.getString("Ability.Generic.Refresh"));
    }

    @Override
    protected void sendSuccessMessage(CommandSender sender, String playerName) {
        sender.sendMessage(LocaleLoader.getString("Commands.mcrefresh.Success", playerName));
    }
}
