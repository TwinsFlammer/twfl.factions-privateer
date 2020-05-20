package br.com.twinsflammer.factionscaribe.mcmmo.skills.axes;

import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.AbilityType;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SecondaryAbility;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.ToolType;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.skills.SkillManager;
import br.com.twinsflammer.factionscaribe.mcmmo.util.ItemUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.Permissions;
import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionscaribe.mcmmo.util.skills.CombatUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.skills.ParticleEffectUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.skills.SkillUtils;
import java.util.Map;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;

public class AxesManager extends SkillManager {

    public AxesManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, SkillType.AXES);
    }

    public boolean canUseAxeMastery() {
        return Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.AXE_MASTERY);
    }

    public boolean canCriticalHit(LivingEntity target) {
        return target.isValid() && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.CRITICAL_HIT);
    }

    public boolean canImpact(LivingEntity target) {
        return target.isValid() && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.ARMOR_IMPACT) && Axes.hasArmor(target);
    }

    public boolean canGreaterImpact(LivingEntity target) {
        return target.isValid() && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.GREATER_IMPACT) && !Axes.hasArmor(target);
    }

    public boolean canUseSkullSplitter(LivingEntity target) {
        return target.isValid() && mcMMOPlayer.getAbilityMode(AbilityType.SKULL_SPLITTER) && Permissions.skullSplitter(getPlayer());
    }

    public boolean canActivateAbility() {
        return mcMMOPlayer.getToolPreparationMode(ToolType.AXE) && Permissions.skullSplitter(getPlayer());
    }

    /**
     * Handle the effects of the Axe Mastery ability
     */
    public double axeMastery() {
        if (!SkillUtils.activationSuccessful(SecondaryAbility.AXE_MASTERY, getPlayer())) {
            return 0;
        }

        return Math.min(getSkillLevel() / (Axes.axeMasteryMaxBonusLevel / Axes.axeMasteryMaxBonus), Axes.axeMasteryMaxBonus);
    }

    /**
     * Handle the effects of the Critical Hit ability
     *
     * @param target The {@link LivingEntity} being affected by the ability
     * @param damage The amount of damage initially dealt by the event
     */
    public double criticalHit(LivingEntity target, double damage) {
        if (!SkillUtils.activationSuccessful(SecondaryAbility.CRITICAL_HIT, getPlayer(), getSkillLevel(), activationChance)) {
            return 0;
        }

        Player player = getPlayer();

        if (mcMMOPlayer.useChatNotifications()) {
            player.sendMessage(LocaleLoader.getString("Axes.Combat.CriticalHit"));
        }

        if (target instanceof Player) {
            Player defender = (Player) target;

            if (UserManager.getPlayer(defender).useChatNotifications()) {
                defender.sendMessage(LocaleLoader.getString("Axes.Combat.CritStruck"));
            }

            damage = (damage * Axes.criticalHitPVPModifier) - damage;
        } else {
            damage = (damage * Axes.criticalHitPVEModifier) - damage;
        }

        return damage;
    }

    /**
     * Handle the effects of the Impact ability
     *
     * @param target The {@link LivingEntity} being affected by Impact
     */
    public void impactCheck(LivingEntity target) {
        int durabilityDamage = 1 + (getSkillLevel() / Axes.impactIncreaseLevel);

        for (ItemStack armor : target.getEquipment().getArmorContents()) {
            if (ItemUtils.isArmor(armor)) {
                if (SkillUtils.activationSuccessful(SecondaryAbility.ARMOR_IMPACT, getPlayer(), Axes.impactChance, activationChance)) {
                    SkillUtils.handleDurabilityChange(armor, durabilityDamage, Axes.impactMaxDurabilityModifier);
                }
            }
        }
    }

    /**
     * Handle the effects of the Greater Impact ability
     *
     * @param target The {@link LivingEntity} being affected by the ability
     */
    public double greaterImpact(LivingEntity target) {
        if (!SkillUtils.activationSuccessful(SecondaryAbility.GREATER_IMPACT, getPlayer(), Axes.greaterImpactChance, activationChance)) {
            return 0;
        }

        Player player = getPlayer();

        ParticleEffectUtils.playGreaterImpactEffect(target);
        target.setVelocity(player.getLocation().getDirection().normalize().multiply(Axes.greaterImpactKnockbackMultiplier));

        if (mcMMOPlayer.useChatNotifications()) {
            player.sendMessage(LocaleLoader.getString("Axes.Combat.GI.Proc"));
        }

        if (target instanceof Player) {
            Player defender = (Player) target;

            if (UserManager.getPlayer(defender).useChatNotifications()) {
                defender.sendMessage(LocaleLoader.getString("Axes.Combat.GI.Struck"));
            }
        }

        return Axes.greaterImpactBonusDamage;
    }

    /**
     * Handle the effects of the Skull Splitter ability
     *
     * @param target The {@link LivingEntity} being affected by the ability
     * @param damage The amount of damage initially dealt by the event
     */
    public void skullSplitterCheck(LivingEntity target, double damage, Map<DamageModifier, Double> modifiers) {
        CombatUtils.applyAbilityAoE(getPlayer(), target, damage / Axes.skullSplitterModifier, modifiers, skill);
    }
}
