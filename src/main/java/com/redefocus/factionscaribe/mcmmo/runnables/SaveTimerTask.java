package com.redefocus.factionscaribe.mcmmo.runnables;

import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.party.PartyManager;
import com.redefocus.factionscaribe.mcmmo.runnables.player.PlayerProfileSaveTask;
import com.redefocus.factionscaribe.mcmmo.util.player.UserManager;
import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTimerTask extends BukkitRunnable {

    @Override
    public void run() {
        // All player data will be saved periodically through this
        int count = 1;

        for (McMMOPlayer mcMMOPlayer : UserManager.getPlayers()) {
            new PlayerProfileSaveTask(mcMMOPlayer.getProfile()).runTaskLaterAsynchronously(FactionsCaribe.getInstance(), count);
            count++;
        }

        PartyManager.saveParties();
    }
}
