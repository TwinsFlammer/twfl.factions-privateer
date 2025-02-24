package br.com.twinsflammer.factionsprivateer.mcmmo.skills.archery;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.AdvancedConfig;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.experience.ExperienceConfig;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Misc;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Archery {

    private static List<TrackedEntity> trackedEntities = new ArrayList<TrackedEntity>();

    public static int skillShotIncreaseLevel = AdvancedConfig.getInstance().getSkillShotIncreaseLevel();
    public static double skillShotIncreasePercentage = AdvancedConfig.getInstance().getSkillShotIncreasePercentage();
    public static double skillShotMaxBonusPercentage = AdvancedConfig.getInstance().getSkillShotBonusMax();
    public static double skillShotMaxBonusDamage = AdvancedConfig.getInstance().getSkillShotDamageMax();

    public static double dazeBonusDamage = AdvancedConfig.getInstance().getDazeBonusDamage();

    public static final double DISTANCE_XP_MULTIPLIER = ExperienceConfig.getInstance().getArcheryDistanceMultiplier();

    protected static void incrementTrackerValue(LivingEntity livingEntity) {
        for (TrackedEntity trackedEntity : trackedEntities) {
            if (trackedEntity.getLivingEntity().getEntityId() == livingEntity.getEntityId()) {
                trackedEntity.incrementArrowCount();
                return;
            }
        }

        addToTracker(livingEntity); // If the entity isn't tracked yet
    }

    protected static void addToTracker(LivingEntity livingEntity) {
        TrackedEntity trackedEntity = new TrackedEntity(livingEntity);

        trackedEntity.incrementArrowCount();
        trackedEntities.add(trackedEntity);
    }

    protected static void removeFromTracker(TrackedEntity trackedEntity) {
        trackedEntities.remove(trackedEntity);
    }

    /**
     * Check for arrow retrieval.
     *
     * @param livingEntity The entity hit by the arrows
     */
    public static void arrowRetrievalCheck(LivingEntity livingEntity) {
        for (Iterator<TrackedEntity> entityIterator = trackedEntities.iterator(); entityIterator.hasNext();) {
            TrackedEntity trackedEntity = entityIterator.next();

            if (trackedEntity.getID() == livingEntity.getUniqueId()) {
                Misc.dropItems(livingEntity.getLocation(), new ItemStack(Material.ARROW), trackedEntity.getArrowCount());
                entityIterator.remove();
                return;
            }
        }
    }
}
