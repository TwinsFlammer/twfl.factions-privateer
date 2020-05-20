package br.com.twinsflammer.factionscaribe.mcmmo.runnables.skills;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.mcmmo.config.AdvancedConfig;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.util.skills.CombatUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.skills.ParticleEffectUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BleedTimerTask extends BukkitRunnable {

    private final static int MAX_BLEED_TICKS = 10;
    private static Map<LivingEntity, Integer> bleedList = new HashMap<LivingEntity, Integer>();

    @Override
    public void run() {
        for (Iterator<Entry<LivingEntity, Integer>> bleedIterator = bleedList.entrySet().iterator(); bleedIterator.hasNext();) {
            Entry<LivingEntity, Integer> entry = bleedIterator.next();
            LivingEntity entity = entry.getKey();

            if (entry.getValue() <= 0 || !entity.isValid()) {
                bleedIterator.remove();
                continue;
            }

            double damage;

            if (entity instanceof Player) {
                damage = AdvancedConfig.getInstance().getBleedDamagePlayer();
                Player player = (Player) entity;

                if (!player.isOnline()) {
                    continue;
                }

                // Never kill with Bleeding
                if (player.getHealth() - damage > 0) {
                    Bukkit.getScheduler().runTask(FactionsCaribe.getInstance(), () -> {
                        CombatUtils.dealDamage(player, damage);
                        ParticleEffectUtils.playBleedEffect(entity);
                    });
                }

                entry.setValue(entry.getValue() - 1);

                if (entry.getValue() <= 0) {
                    player.sendMessage(LocaleLoader.getString("Swords.Combat.Bleeding.Stopped"));
                }
            } else {
                damage = AdvancedConfig.getInstance().getBleedDamageMobs();

                // Anticipate the entity's death to prevent CME because of our EntityDeathEvent listener
                if (entity.getHealth() - damage > 0) {
                    entry.setValue(entry.getValue() - 1);
                } else {
                    bleedIterator.remove();
                }

                Bukkit.getScheduler().runTask(FactionsCaribe.getInstance(), () -> {
                    CombatUtils.dealDamage(entity, damage);
                    ParticleEffectUtils.playBleedEffect(entity);
                });
            }
        }
    }

    /**
     * Instantly Bleed out a LivingEntity
     *
     * @param entity LivingEntity to bleed out
     */
    public static void bleedOut(LivingEntity entity) {
        if (bleedList.containsKey(entity)) {
            CombatUtils.dealDamage(entity, bleedList.get(entity) * 2);
            bleedList.remove(entity);
        }
    }

    /**
     * Remove a LivingEntity from the bleedList if it is in it
     *
     * @param entity LivingEntity to remove
     */
    public static void remove(LivingEntity entity) {
        if (bleedList.containsKey(entity)) {
            bleedList.remove(entity);
        }
    }

    /**
     * Add a LivingEntity to the bleedList if it is not in it.
     *
     * @param entity LivingEntity to add
     * @param ticks Number of bleeding ticks
     */
    public static void add(LivingEntity entity, int ticks) {
        int newTicks = ticks;

        if (bleedList.containsKey(entity)) {
            newTicks += bleedList.get(entity);
            bleedList.put(entity, Math.min(newTicks, MAX_BLEED_TICKS));
        } else {
            bleedList.put(entity, Math.min(newTicks, MAX_BLEED_TICKS));
        }
    }

    public static boolean isBleeding(LivingEntity entity) {
        return bleedList.containsKey(entity);
    }
}
