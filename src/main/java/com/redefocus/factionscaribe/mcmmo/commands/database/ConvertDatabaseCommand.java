package com.redefocus.factionscaribe.mcmmo.commands.database;

import com.redefocus.factionscaribe.mcmmo.database.DatabaseManager;
import com.redefocus.factionscaribe.mcmmo.database.DatabaseManagerFactory;
import com.redefocus.factionscaribe.mcmmo.datatypes.database.DatabaseType;
import com.redefocus.factionscaribe.mcmmo.datatypes.player.PlayerProfile;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.mcmmo.runnables.database.DatabaseConversionTask;
import com.redefocus.factionscaribe.mcmmo.runnables.player.PlayerProfileLoadingTask;
import com.redefocus.factionscaribe.mcmmo.util.player.UserManager;
import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConvertDatabaseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 2:
                DatabaseType previousType = DatabaseType.getDatabaseType(args[1]);
                DatabaseType newType = mcMMO.getDatabaseManager().getDatabaseType();

                if (previousType == newType || (newType == DatabaseType.CUSTOM && DatabaseManagerFactory.getCustomDatabaseManagerClass().getSimpleName().equalsIgnoreCase(args[1]))) {
                    sender.sendMessage(LocaleLoader.getString("Commands.mcconvert.Database.Same", newType.toString()));
                    return true;
                }

                DatabaseManager oldDatabase = DatabaseManagerFactory.createDatabaseManager(previousType);

                if (previousType == DatabaseType.CUSTOM) {
                    Class<?> clazz;

                    try {
                        clazz = Class.forName(args[1]);

                        if (!DatabaseManager.class.isAssignableFrom(clazz)) {
                            sender.sendMessage(LocaleLoader.getString("Commands.mcconvert.Database.InvalidType", args[1]));
                            return true;
                        }

                        oldDatabase = DatabaseManagerFactory.createCustomDatabaseManager((Class<? extends DatabaseManager>) clazz);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        sender.sendMessage(LocaleLoader.getString("Commands.mcconvert.Database.InvalidType", args[1]));
                        return true;
                    }
                }

                sender.sendMessage(LocaleLoader.getString("Commands.mcconvert.Database.Start", previousType.toString(), newType.toString()));

                UserManager.saveAll();
                UserManager.clearAll();

                for (Player player : FactionsCaribe.getInstance().getServer().getOnlinePlayers()) {
                    PlayerProfile profile = oldDatabase.loadPlayerProfile(player.getUniqueId());

                    if (profile.isLoaded()) {
                        mcMMO.getDatabaseManager().saveUser(profile);
                    }

                    new PlayerProfileLoadingTask(player).runTaskLaterAsynchronously(FactionsCaribe.getInstance(), 1); // 1 Tick delay to ensure the player is marked as online before we begin loading
                }

                new DatabaseConversionTask(oldDatabase, sender, previousType.toString(), newType.toString()).runTaskAsynchronously(FactionsCaribe.getInstance());
                return true;

            default:
                return false;
        }
    }
}
