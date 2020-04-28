package com.redefocus.factionscaribe.mcmmo.commands.database;

import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.google.common.collect.ImmutableList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class McpurgeCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 0:
                mcMMO.getDatabaseManager().purgePowerlessUsers();

                if (Config.getInstance().getOldUsersCutoff() != -1) {
                    mcMMO.getDatabaseManager().purgeOldUsers();
                }

                sender.sendMessage(LocaleLoader.getString("Commands.mcpurge.Success"));
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
