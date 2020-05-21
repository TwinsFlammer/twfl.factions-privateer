package br.com.twinsflammer.factionsprivateer.mcmmo.runnables;

import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdaterResultAsyncTask extends BukkitRunnable {

    private mcMMO plugin;

    public UpdaterResultAsyncTask(mcMMO plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        /*Updater updater = new Updater(Server.getInstance(), 31030, plugin.mcmmo, Updater.UpdateType.NO_DOWNLOAD, false);

        if (updater.getResult() != Updater.UpdateResult.UPDATE_AVAILABLE) {
            plugin.setUpdateAvailable(false);
            return;
        }

        if (updater.getLatestType().equals("beta") && !Config.getInstance().getPreferBeta()) {
            plugin.setUpdateAvailable(false);
            return;
        }

        plugin.setUpdateAvailable(true);
        Server.getInstance().getLogger().info(LocaleLoader.getString("UpdateChecker.Outdated"));
        Server.getInstance().getLogger().info(LocaleLoader.getString("UpdateChecker.NewAvailable"));*/
    }
}
