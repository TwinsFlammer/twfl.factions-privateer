package com.redefocus.factionscaribe.mcmmo.runnables.player;

import com.redefocus.factionscaribe.mcmmo.util.scoreboards.ScoreboardManager;
import org.bukkit.scheduler.BukkitRunnable;

public class PowerLevelUpdatingTask extends BukkitRunnable {

    @Override
    public void run() {
        if (!ScoreboardManager.powerLevelHeartbeat()) {
            this.cancel();
        }
    }
}
