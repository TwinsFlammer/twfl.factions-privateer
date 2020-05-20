package br.com.twinsflammer.factionscaribe.mcmmo.commands.player;

import br.com.twinsflammer.factionscaribe.mcmmo.config.Config;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.AbilityType;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.util.commands.CommandUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionscaribe.mcmmo.util.scoreboards.ScoreboardManager;
import com.google.common.collect.ImmutableList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class MccooldownCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (CommandUtils.noConsoleUsage(sender)) {
            return true;
        }

        if (!CommandUtils.hasPlayerDataKey(sender)) {
            return true;
        }

        switch (args.length) {
            case 0:
                Player player = (Player) sender;

                if (Config.getInstance().getCooldownUseBoard()) {
                    ScoreboardManager.enablePlayerCooldownScoreboard(player);

                    if (!Config.getInstance().getCooldownUseChat()) {
                        return true;
                    }
                }

                McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);

                player.sendMessage(LocaleLoader.getString("Commands.Cooldowns.Header"));
                player.sendMessage(LocaleLoader.getString("mcMMO.NoSkillNote"));

                for (AbilityType ability : AbilityType.values()) {
                    if (!ability.getPermissions(player)) {
                        continue;
                    }

                    int seconds = mcMMOPlayer.calculateTimeRemaining(ability);

                    if (seconds <= 0) {
                        player.sendMessage(LocaleLoader.getString("Commands.Cooldowns.Row.Y", ability.getName()));
                    } else {
                        player.sendMessage(LocaleLoader.getString("Commands.Cooldowns.Row.N", ability.getName(), seconds));
                    }
                }

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
