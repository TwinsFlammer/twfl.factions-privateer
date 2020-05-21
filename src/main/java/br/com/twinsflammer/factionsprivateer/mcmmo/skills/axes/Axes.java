package br.com.twinsflammer.factionsprivateer.mcmmo.skills.axes;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.AdvancedConfig;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.ItemUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class Axes {

    public static double axeMasteryMaxBonus = AdvancedConfig.getInstance().getAxeMasteryBonusMax();
    public static int axeMasteryMaxBonusLevel = AdvancedConfig.getInstance().getAxeMasteryMaxBonusLevel();

    public static double criticalHitPVPModifier = AdvancedConfig.getInstance().getCriticalHitPVPModifier();
    public static double criticalHitPVEModifier = AdvancedConfig.getInstance().getCriticalHitPVEModifier();

    public static int impactIncreaseLevel = AdvancedConfig.getInstance().getArmorImpactIncreaseLevel();
    public static double impactChance = AdvancedConfig.getInstance().getImpactChance();
    public static double impactMaxDurabilityModifier = AdvancedConfig.getInstance().getArmorImpactMaxDurabilityDamage() / 100D;

    public static double greaterImpactBonusDamage = AdvancedConfig.getInstance().getGreaterImpactBonusDamage();
    public static double greaterImpactChance = AdvancedConfig.getInstance().getGreaterImpactChance();
    public static double greaterImpactKnockbackMultiplier = AdvancedConfig.getInstance().getGreaterImpactModifier();

    public static double skullSplitterModifier = AdvancedConfig.getInstance().getSkullSplitterModifier();

    protected static boolean hasArmor(LivingEntity target) {
        for (ItemStack itemStack : target.getEquipment().getArmorContents()) {
            if (ItemUtils.isArmor(itemStack)) {
                return true;
            }
        }

        return false;
    }
}
