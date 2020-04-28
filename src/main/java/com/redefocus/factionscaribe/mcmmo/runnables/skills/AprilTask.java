package com.redefocus.factionscaribe.mcmmo.runnables.skills;

import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.mcmmo.util.HolidayManager;
import com.redefocus.factionscaribe.mcmmo.util.Misc;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AprilTask extends BukkitRunnable {

    @Override
    public void run() {
        if (!mcMMO.getHolidayManager().isAprilFirst()) {
            this.cancel();
            return;
        }

        for (Player player : FactionsCaribe.getInstance().getServer().getOnlinePlayers()) {
            int random = Misc.getRandom().nextInt(40) + 11;
            int betterRandom = Misc.getRandom().nextInt(2000);
            if (betterRandom == 0) {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, Misc.LEVELUP_VOLUME, Misc.LEVELUP_PITCH);
                player.sendMessage(unknown("superskill") + " skill increased by 1. Total (" + unknown("12") + ")");
                fireworksShow(player);
            }

            for (Statistic statistic : mcMMO.getHolidayManager().movementStatistics) {
                if (player.getStatistic(statistic) > 0 && player.getStatistic(statistic) % random == 0) {
                    mcMMO.getHolidayManager().levelUpApril(player, HolidayManager.FakeSkillType.getByStatistic(statistic));
                    break;
                }
            }
        }
    }

    private void fireworksShow(final Player player) {
        final int firework_amount = 10;
        for (int i = 0; i < firework_amount; i++) {
            int delay = (int) (Misc.getRandom().nextDouble() * 3 * Misc.TICK_CONVERSION_FACTOR) + 4;
            FactionsCaribe.getInstance().getServer().getScheduler().runTaskLater(FactionsCaribe.getInstance(), new Runnable() {
                @Override
                public void run() {
                    mcMMO.getHolidayManager().spawnFireworks(player);
                }
            }, delay);
        }
    }

    private String unknown(String string) {
        return ChatColor.MAGIC + string + ChatColor.RESET + ChatColor.YELLOW;
    }
}
