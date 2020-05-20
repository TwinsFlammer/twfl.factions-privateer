package br.com.twinsflammer.factionscaribe.mcmmo.runnables;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.twinsflammer.factionscaribe.mcmmo.mcMMO;
import br.com.twinsflammer.factionscaribe.mcmmo.runnables.skills.AprilTask;
import br.com.twinsflammer.factionscaribe.mcmmo.util.Misc;

public class CheckDateTask extends BukkitRunnable {

    @Override
    public void run() {
        if (!mcMMO.getHolidayManager().isAprilFirst()) {
            return;
        }

        // Set up jokes
        new AprilTask().runTaskTimer(FactionsCaribe.getInstance(), 1L * 60L * Misc.TICK_CONVERSION_FACTOR, 10L * 60L * Misc.TICK_CONVERSION_FACTOR);
        mcMMO.getHolidayManager().registerAprilCommand();

        // Jokes deployed.
        this.cancel();
    }
}
