package br.com.twinsflammer.factionscaribe.mcmmo.runnables.player;

import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class ClearRegisteredXPGainTask extends BukkitRunnable {

    @Override
    public void run() {
        for (McMMOPlayer mcMMOPlayer : UserManager.getPlayers()) {
            mcMMOPlayer.getProfile().purgeExpiredXpGains();
        }
    }
}
