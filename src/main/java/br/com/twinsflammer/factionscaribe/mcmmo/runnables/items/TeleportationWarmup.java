package br.com.twinsflammer.factionscaribe.mcmmo.runnables.items;

import br.com.twinsflammer.factionscaribe.mcmmo.config.Config;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.party.PartyManager;
import br.com.twinsflammer.factionscaribe.mcmmo.util.EventUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.Misc;
import br.com.twinsflammer.factionscaribe.mcmmo.util.skills.SkillUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportationWarmup extends BukkitRunnable {

    private McMMOPlayer mcMMOPlayer;
    private McMMOPlayer mcMMOTarget;

    public TeleportationWarmup(McMMOPlayer mcMMOPlayer, McMMOPlayer mcMMOTarget) {
        this.mcMMOPlayer = mcMMOPlayer;
        this.mcMMOTarget = mcMMOTarget;
    }

    @Override
    public void run() {
        Player teleportingPlayer = mcMMOPlayer.getPlayer();
        Player targetPlayer = mcMMOTarget.getPlayer();
        Location previousLocation = mcMMOPlayer.getTeleportCommenceLocation();
        Location newLocation = mcMMOPlayer.getPlayer().getLocation();
        long recentlyHurt = mcMMOPlayer.getRecentlyHurt();

        mcMMOPlayer.setTeleportCommenceLocation(null);

        if (!PartyManager.inSameParty(teleportingPlayer, targetPlayer)) {
            teleportingPlayer.sendMessage(LocaleLoader.getString("Party.NotInYourParty", targetPlayer.getName()));
            return;
        }

        if (newLocation.distanceSquared(previousLocation) > 1.0) {
            teleportingPlayer.sendMessage(LocaleLoader.getString("Teleport.Cancelled"));
            return;
        }

        int hurtCooldown = Config.getInstance().getPTPCommandRecentlyHurtCooldown();

        if (hurtCooldown > 0) {
            int timeRemaining = SkillUtils.calculateTimeLeft(recentlyHurt * Misc.TIME_CONVERSION_FACTOR, hurtCooldown, teleportingPlayer);

            if (timeRemaining > 0) {
                teleportingPlayer.sendMessage(LocaleLoader.getString("Item.Injured.Wait", timeRemaining));
                return;
            }
        }

        EventUtils.handlePartyTeleportEvent(teleportingPlayer, targetPlayer);
    }
}
