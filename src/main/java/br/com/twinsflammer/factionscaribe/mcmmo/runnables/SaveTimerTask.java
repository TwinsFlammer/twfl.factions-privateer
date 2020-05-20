package br.com.twinsflammer.factionscaribe.mcmmo.runnables;

import br.com.twinsflammer.factionscaribe.mcmmo.party.PartyManager;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.runnables.player.PlayerProfileSaveTask;
import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionscaribe.FactionsCaribe;
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
