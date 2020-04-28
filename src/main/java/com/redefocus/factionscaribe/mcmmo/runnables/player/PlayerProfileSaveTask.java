package com.redefocus.factionscaribe.mcmmo.runnables.player;

import com.redefocus.factionscaribe.mcmmo.datatypes.player.PlayerProfile;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerProfileSaveTask extends BukkitRunnable {

    private PlayerProfile playerProfile;

    public PlayerProfileSaveTask(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    @Override
    public void run() {
        playerProfile.save();
    }
}
