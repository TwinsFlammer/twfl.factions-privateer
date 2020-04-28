package com.redefocus.factionscaribe.mcmmo.runnables.player;

import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.util.player.UserManager;
import org.bukkit.scheduler.BukkitRunnable;

public class ClearRegisteredXPGainTask extends BukkitRunnable {

    @Override
    public void run() {
        for (McMMOPlayer mcMMOPlayer : UserManager.getPlayers()) {
            mcMMOPlayer.getProfile().purgeExpiredXpGains();
        }
    }
}
