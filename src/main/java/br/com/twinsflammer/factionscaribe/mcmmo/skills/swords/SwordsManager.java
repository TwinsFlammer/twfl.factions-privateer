package br.com.twinsflammer.factionscaribe.mcmmo.skills.swords;

import br.com.twinsflammer.factionscaribe.mcmmo.config.AdvancedConfig;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.AbilityType;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SecondaryAbility;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.ToolType;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.runnables.skills.BleedTimerTask;
import br.com.twinsflammer.factionscaribe.mcmmo.skills.SkillManager;
import br.com.twinsflammer.factionscaribe.mcmmo.util.Permissions;
import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionscaribe.mcmmo.util.skills.CombatUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.skills.SkillUtils;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

public class SwordsManager extends SkillManager {

    public SwordsManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, SkillType.SWORDS);
    }

    public boolean canActivateAbility() {
        return mcMMOPlayer.getToolPreparationMode(ToolType.SWORD) && Permissions.serratedStrikes(getPlayer());
    }

    public boolean canUseBleed() {
        return Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.BLEED);
    }

    public boolean canUseCounterAttack(Entity target) {
        return target instanceof LivingEntity && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.COUNTER);
    }

    public boolean canUseSerratedStrike() {
        return mcMMOPlayer.getAbilityMode(AbilityType.SERRATED_STRIKES) && Permissions.serratedStrikes(getPlayer());
    }

    /**
     * Check for Bleed effect.
     *
     * @param target The defending entity
     */
    public void bleedCheck(LivingEntity target) {
        if (SkillUtils.activationSuccessful(SecondaryAbility.BLEED, getPlayer(), getSkillLevel(), activationChance)) {

            if (getSkillLevel() >= AdvancedConfig.getInstance().getMaxBonusLevel(SecondaryAbility.BLEED)) {
                BleedTimerTask.add(target, Swords.bleedMaxTicks);
            } else {
                BleedTimerTask.add(target, Swords.bleedBaseTicks);
            }

            if (mcMMOPlayer.useChatNotifications()) {
                getPlayer().sendMessage(LocaleLoader.getString("Swords.Combat.Bleeding"));
            }

            if (target instanceof Player) {
                Player defender = (Player) target;

                if (UserManager.getPlayer(defender).useChatNotifications()) {
                    defender.sendMessage(LocaleLoader.getString("Swords.Combat.Bleeding.Started"));
                }
            }
        }
    }

    /**
     * Handle the effects of the Counter Attack ability
     *
     * @param attacker The {@link LivingEntity} being affected by the ability
     * @param damage The amount of damage initially dealt by the event
     */
    public void counterAttackChecks(LivingEntity attacker, double damage) {
        if (Swords.counterAttackRequiresBlock && !getPlayer().isBlocking()) {
            return;
        }

        if (SkillUtils.activationSuccessful(SecondaryAbility.COUNTER, getPlayer(), getSkillLevel(), activationChance)) {
            CombatUtils.dealDamage(attacker, damage / Swords.counterAttackModifier, getPlayer());

            getPlayer().sendMessage(LocaleLoader.getString("Swords.Combat.Countered"));

            if (attacker instanceof Player) {
                ((Player) attacker).sendMessage(LocaleLoader.getString("Swords.Combat.Counter.Hit"));
            }
        }
    }

    /**
     * Handle the effects of the Serrated Strikes ability
     *
     * @param target The {@link LivingEntity} being affected by the ability
     * @param damage The amount of damage initially dealt by the event
     */
    public void serratedStrikes(LivingEntity target, double damage, Map<DamageModifier, Double> modifiers) {
        CombatUtils.applyAbilityAoE(getPlayer(), target, damage / Swords.serratedStrikesModifier, modifiers, skill);
        BleedTimerTask.add(target, Swords.serratedStrikesBleedTicks);
    }
}
