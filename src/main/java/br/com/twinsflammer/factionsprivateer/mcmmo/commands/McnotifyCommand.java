package br.com.twinsflammer.factionsprivateer.mcmmo.commands;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import com.google.common.collect.ImmutableList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class McnotifyCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 0:
                McMMOPlayer mcMMOPlayer = UserManager.getPlayer((Player) sender);

                sender.sendMessage(LocaleLoader.getString("Commands.Notifications." + (mcMMOPlayer.useChatNotifications() ? "Off" : "On")));
                mcMMOPlayer.toggleChatNotifications();
                return true;

            default:
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return ImmutableList.of();
    }
}
