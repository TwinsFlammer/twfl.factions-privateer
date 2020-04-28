package com.redefocus.factionscaribe.mcmmo.runnables.commands;

import com.redefocus.factionscaribe.mcmmo.util.scoreboards.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class McScoreboardKeepTask extends BukkitRunnable {

    private Player player;

    public McScoreboardKeepTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (player.isValid() && ScoreboardManager.isBoardShown(player.getName())) {
            ScoreboardManager.keepBoard(player.getName());
        }
    }
}
