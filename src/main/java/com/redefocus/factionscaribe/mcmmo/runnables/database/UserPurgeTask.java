package com.redefocus.factionscaribe.mcmmo.runnables.database;

import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
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
