package br.com.twinsflammer.factionsprivateer.mcmmo.runnables.database;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;

import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.scheduler.BukkitRunnable;

public class UserPurgeTask extends BukkitRunnable {

    private ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        lock.lock();
        mcMMO.getDatabaseManager().purgePowerlessUsers();

        if (Config.getInstance().getOldUsersCutoff() != -1) {
            mcMMO.getDatabaseManager().purgeOldUsers();
        }
        lock.unlock();
    }
}
