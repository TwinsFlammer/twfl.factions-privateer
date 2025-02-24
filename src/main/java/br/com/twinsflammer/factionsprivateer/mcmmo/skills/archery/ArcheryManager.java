package br.com.twinsflammer.factionsprivateer.mcmmo.skills.archery;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SecondaryAbility;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.SkillManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Misc;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Permissions;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.skills.SkillUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ArcheryManager extends SkillManager {

    public ArcheryManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, SkillType.ARCHERY);
    }

    public boolean canDaze(LivingEntity target) {
        return target instanceof Player && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.DAZE);
    }

    public boolean canSkillShot() {
        return getSkillLevel() >= Archery.skillShotIncreaseLevel && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.SKILL_SHOT);
    }

    public boolean canRetrieveArrows() {
        return Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.RETRIEVE);
    }

    /**
     * Calculate bonus XP awarded for Archery when hitting a far-away target.
     *
     * @param target The {@link LivingEntity} damaged by the arrow
     * @param damager The {@link Entity} who shot the arrow
     */
    public void distanceXpBonus(LivingEntity target, Entity damager) {
        Location firedLocation = (Location) damager.getMetadata(mcMMO.arrowDistanceKey).get(0).value();
        Location targetLocation = target.getLocation();

        if (firedLocation.getWorld() != targetLocation.getWorld()) {
            return;
        }

        applyXpGain((int) (Math.min(firedLocation.distanceSquared(targetLocation), 2500) * Archery.DISTANCE_XP_MULTIPLIER), getXPGainReason(target, damager));
    }

    /**
     * Track arrows fired for later retrieval.
     *
     * @param target The {@link LivingEntity} damaged by the arrow
     */
    public void retrieveArrows(LivingEntity target) {
        if (SkillUtils.activationSuccessful(SecondaryAbility.RETRIEVE, getPlayer(), getSkillLevel(), activationChance)) {
            Archery.incrementTrackerValue(target);
        }
    }

    /**
     * Handle the effects of the Daze ability
     *
     * @param defender The {@link Player} being affected by the ability
     */
    public double daze(Player defender) {
        if (!SkillUtils.activationSuccessful(SecondaryAbility.DAZE, getPlayer(), getSkillLevel(), activationChance)) {
            return 0;
        }

        Location dazedLocation = defender.getLocation();
        dazedLocation.setPitch(90 - Misc.getRandom().nextInt(181));

        defender.teleport(dazedLocation);
        defender.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 10));

        if (UserManager.getPlayer(defender).useChatNotifications()) {
            defender.sendMessage(LocaleLoader.getString("Combat.TouchedFuzzy"));
        }

        if (mcMMOPlayer.useChatNotifications()) {
            getPlayer().sendMessage(LocaleLoader.getString("Combat.TargetDazed"));
        }

        return Archery.dazeBonusDamage;
    }

    /**
     * Handle the effects of the Skill Shot ability
     *
     * @param damage The amount of damage initially dealt by the event
     */
    public double skillShot(double damage) {
        if (!SkillUtils.activationSuccessful(SecondaryAbility.SKILL_SHOT, getPlayer())) {
            return damage;
        }

        double damageBonusPercent = Math.min(((getSkillLevel() / Archery.skillShotIncreaseLevel) * Archery.skillShotIncreasePercentage), Archery.skillShotMaxBonusPercentage);

        return Math.min(damage * damageBonusPercent, Archery.skillShotMaxBonusDamage);
    }
}
