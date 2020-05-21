package br.com.twinsflammer.factionsprivateer.mcmmo.runnables.items;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.ChimaeraWing;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.ItemUtils;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Misc;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.skills.SkillUtils;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ChimaeraWingWarmup extends BukkitRunnable {

    private McMMOPlayer mcMMOPlayer;

    public ChimaeraWingWarmup(McMMOPlayer mcMMOPlayer) {
        this.mcMMOPlayer = mcMMOPlayer;
    }

    @Override
    public void run() {
        checkChimaeraWingTeleport();
    }

    private void checkChimaeraWingTeleport() {
        Player player = mcMMOPlayer.getPlayer();
        Location previousLocation = mcMMOPlayer.getTeleportCommenceLocation();

        if (player.getLocation().distanceSquared(previousLocation) > 1.0 || !player.getInventory().containsAtLeast(ChimaeraWing.getChimaeraWing(0), 1)) {
            player.sendMessage(LocaleLoader.getString("Teleport.Cancelled"));
            return;
        }

        ItemStack inHand = player.getItemInHand();

        if (!ItemUtils.isChimaeraWing(inHand) || inHand.getAmount() < Config.getInstance().getChimaeraUseCost()) {
            player.sendMessage(LocaleLoader.getString("Skills.NeedMore", LocaleLoader.getString("Item.ChimaeraWing.Name")));
            return;
        }

        long recentlyHurt = mcMMOPlayer.getRecentlyHurt();
        int hurtCooldown = Config.getInstance().getChimaeraRecentlyHurtCooldown();

        if (hurtCooldown > 0) {
            int timeRemaining = SkillUtils.calculateTimeLeft(recentlyHurt * Misc.TIME_CONVERSION_FACTOR, hurtCooldown, player);

            if (timeRemaining > 0) {
                player.sendMessage(LocaleLoader.getString("Item.Injured.Wait", timeRemaining));
                return;
            }
        }

        ChimaeraWing.chimaeraExecuteTeleport();
    }
}
