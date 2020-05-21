package br.com.twinsflammer.factionsprivateer.mcmmo.runnables;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.mcmmo.runnables.skills.AprilTask;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Misc;

public class CheckDateTask extends BukkitRunnable {

    @Override
    public void run() {
        if (!mcMMO.getHolidayManager().isAprilFirst()) {
            return;
        }

        // Set up jokes
        new AprilTask().runTaskTimer(FactionsPrivateer.getInstance(), 1L * 60L * Misc.TICK_CONVERSION_FACTOR, 10L * 60L * Misc.TICK_CONVERSION_FACTOR);
        mcMMO.getHolidayManager().registerAprilCommand();

        // Jokes deployed.
        this.cancel();
    }
}
