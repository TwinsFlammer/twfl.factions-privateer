package br.com.twinsflammer.factionscaribe.mcmmo.commands.player;

import br.com.twinsflammer.factionscaribe.mcmmo.config.Config;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.mcMMO;
import br.com.twinsflammer.factionscaribe.mcmmo.runnables.commands.McrankCommandAsyncTask;
import br.com.twinsflammer.factionscaribe.mcmmo.util.Permissions;
import br.com.twinsflammer.factionscaribe.mcmmo.util.commands.CommandUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.StringUtil;

public class McrankCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 0:
                if (CommandUtils.noConsoleUsage(sender)) {
                    return true;
                }

                if (!Permissions.mcrank(sender)) {
                    sender.sendMessage(command.getPermissionMessage());
                    return true;
                }

                if (!CommandUtils.hasPlayerDataKey(sender)) {
                    return true;
                }

                display(sender, sender.getName());

                return true;

            case 1:
                if (!Permissions.mcrankOthers(sender)) {
                    sender.sendMessage(command.getPermissionMessage());
                    return true;
                }

                if (!CommandUtils.hasPlayerDataKey(sender)) {
                    return true;
                }

                String playerName = CommandUtils.getMatchedPlayerName(args[0]);
                McMMOPlayer mcMMOPlayer = UserManager.getOfflinePlayer(playerName);

                if (mcMMOPlayer != null) {
                    Player player = mcMMOPlayer.getPlayer();
                    playerName = player.getName();

                    if (CommandUtils.tooFar(sender, player, Permissions.mcrankFar(sender))) {
                        return true;
                    }
                } else if (CommandUtils.inspectOffline(sender, mcMMO.getDatabaseManager().loadPlayerProfile(playerName, false), Permissions.mcrankOffline(sender))) {
                    return true;
                }

                display(sender, playerName);
                return true;

            default:
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1:
                List<String> playerNames = CommandUtils.getOnlinePlayerNames(sender);
                return StringUtil.copyPartialMatches(args[0], playerNames, new ArrayList<String>(playerNames.size()));
            default:
                return ImmutableList.of();
        }
    }

    private void display(CommandSender sender, String playerName) {
        if (sender instanceof Player) {
            McMMOPlayer mcMMOPlayer = UserManager.getPlayer(sender.getName());
            long cooldownMillis = Math.max(Config.getInstance().getDatabasePlayerCooldown(), 1750);

            if (mcMMOPlayer.getDatabaseATS() + cooldownMillis > System.currentTimeMillis()) {
                sender.sendMessage(LocaleLoader.getString("Commands.Database.Cooldown"));
                return;
            }

            if (((Player) sender).hasMetadata(mcMMO.databaseCommandKey)) {
                sender.sendMessage(LocaleLoader.getString("Commands.Database.Processing"));
                return;
            } else {
                ((Player) sender).setMetadata(mcMMO.databaseCommandKey, new FixedMetadataValue(FactionsCaribe.getInstance(), null));
            }

            mcMMOPlayer.actualizeDatabaseATS();
        }

        boolean useBoard = (sender instanceof Player) && (Config.getInstance().getRankUseBoard());
        boolean useChat = !useBoard || Config.getInstance().getRankUseChat();

        new McrankCommandAsyncTask(playerName, sender, useBoard, useChat).runTaskAsynchronously(FactionsCaribe.getInstance());
    }
}
