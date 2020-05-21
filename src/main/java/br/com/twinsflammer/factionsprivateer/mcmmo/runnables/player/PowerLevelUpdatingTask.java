package br.com.twinsflammer.factionsprivateer.mcmmo.runnables.player;

import br.com.twinsflammer.factionsprivateer.mcmmo.util.scoreboards.ScoreboardManager;
import org.bukkit.scheduler.BukkitRunnable;

public class PowerLevelUpdatingTask extends BukkitRunnable {

    @Override
    public void run() {
        if (!ScoreboardManager.powerLevelHeartbeat()) {
            this.cancel();
        }
    }
}
