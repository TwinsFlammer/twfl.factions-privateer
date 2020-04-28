package com.redefocus.factionscaribe.mcmmo.runnables;

import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.scheduler.BukkitRunnable;

import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.mcmmo.runnables.skills.AprilTask;
import com.redefocus.factionscaribe.mcmmo.util.Misc;

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
