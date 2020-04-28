package com.redefocus.factionscaribe.mcmmo.commands.database;

import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.database.DatabaseManagerFactory;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.google.common.collect.ImmutableList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class MmoshowdbCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 0:
                Class<?> clazz = DatabaseManagerFactory.getCustomDatabaseManagerClass();

                if (clazz != null) {
                    sender.sendMessage(LocaleLoader.getString("Commands.mmoshowdb", clazz.getName()));
                    return true;
                }

                sender.sendMessage(LocaleLoader.getString("Commands.mmoshowdb", (Config.getInstance().getUseMySQL() ? "sql" : "flatfile")));
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
