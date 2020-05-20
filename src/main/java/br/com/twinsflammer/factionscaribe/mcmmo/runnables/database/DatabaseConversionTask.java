package br.com.twinsflammer.factionscaribe.mcmmo.runnables.database;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.mcmmo.database.DatabaseManager;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.mcMMO;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class DatabaseConversionTask extends BukkitRunnable {

    private final DatabaseManager sourceDatabase;
    private final CommandSender sender;
    private final String message;

    public DatabaseConversionTask(DatabaseManager sourceDatabase, CommandSender sender, String oldType, String newType) {
        this.sourceDatabase = sourceDatabase;
        this.sender = sender;
        message = LocaleLoader.getString("Commands.mcconvert.Database.Finish", oldType, newType);
    }

    @Override
    public void run() {
        sourceDatabase.convertUsers(mcMMO.getDatabaseManager());

        FactionsCaribe.getInstance().getServer().getScheduler().runTask(FactionsCaribe.getInstance(), new Runnable() {
            @Override
            public void run() {
                sender.sendMessage(message);
            }
        });
    }
}
