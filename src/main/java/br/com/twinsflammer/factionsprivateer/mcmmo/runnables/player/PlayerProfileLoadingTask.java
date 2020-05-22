package br.com.twinsflammer.factionsprivateer.mcmmo.runnables.player;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.PlayerProfile;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.mcmmo.runnables.commands.McScoreboardKeepTask;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Misc;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.scoreboards.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerProfileLoadingTask extends BukkitRunnable {

    private static final int MAX_TRIES = 5;
    private final Player player;
    private int attempt = 0;

    public PlayerProfileLoadingTask(Player player) {
        this.player = player;
    }

    private PlayerProfileLoadingTask(Player player, int attempt) {
        this.player = player;
        this.attempt = attempt;
    }

    // WARNING: ASYNC TASK
    // DO NOT MODIFY THE McMMOPLAYER FROM THIS CODE
    @Override
    public void run() {
        // Quit if they logged out
        if (!player.isOnline()) {
            FactionsPrivateer.getInstance().getLogger().info("Aborting profile loading recovery for " + player.getName() + " - player logged out");
            return;
        }

        // Increment attempt counter and try
        attempt++;

        PlayerProfile profile = mcMMO.getDatabaseManager().loadPlayerProfile(player.getName(), player.getUniqueId(), true);
        // If successful, schedule the apply
        if (profile.isLoaded()) {
            new ApplySuccessfulProfile(new McMMOPlayer(player, profile)).runTask(FactionsPrivateer.getInstance());
            return;
        }

        // If we've failed five times, give up
        if (attempt >= MAX_TRIES) {
            FactionsPrivateer.getInstance().getLogger().severe("Giving up on attempting to load the PlayerProfile for " + player.getName());
            FactionsPrivateer.getInstance().getServer().broadcast(LocaleLoader.getString("Profile.Loading.AdminFailureNotice", player.getName()), "TESTE");
            player.sendMessage(LocaleLoader.getString("Profile.Loading.Failure").split("\n"));
            return;
        }
        new PlayerProfileLoadingTask(player, attempt).runTaskLaterAsynchronously(FactionsPrivateer.getInstance(), 100 * attempt);
    }

    private class ApplySuccessfulProfile extends BukkitRunnable {

        private final McMMOPlayer mcMMOPlayer;

        private ApplySuccessfulProfile(McMMOPlayer mcMMOPlayer) {
            this.mcMMOPlayer = mcMMOPlayer;
        }

        // Synchronized task
        // No database access permitted
        @Override
        public void run() {
            if (!player.isOnline()) {
                FactionsPrivateer.getInstance().getLogger().info("Aborting profile loading recovery for " + player.getName() + " - player logged out");
                return;
            }

            mcMMOPlayer.setupPartyData();
            UserManager.track(mcMMOPlayer);
            mcMMOPlayer.actualizeRespawnATS();
            ScoreboardManager.setupPlayer(player);

            if (Config.getInstance().getShowProfileLoadedMessage()) {
                player.sendMessage(LocaleLoader.getString("Profile.Loading.Success"));
            }

            if (Config.getInstance().getShowStatsAfterLogin()) {
                ScoreboardManager.enablePlayerStatsScoreboard(player);
                new McScoreboardKeepTask(player).runTaskLater(FactionsPrivateer.getInstance(), 1 * Misc.TICK_CONVERSION_FACTOR);
            }
        }
    }
}
