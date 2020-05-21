package br.com.twinsflammer.factionsprivateer.mcmmo.listeners;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.experience.ExperienceConfig;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.experience.McMMOPlayerLevelUpEvent;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.experience.McMMOPlayerXpGainEvent;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.scoreboards.ScoreboardManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SelfListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLevelUp(McMMOPlayerLevelUpEvent event) {
        Player player = event.getPlayer();
        SkillType skill = event.getSkill();

        ScoreboardManager.handleLevelUp(player, skill);

        if (!Config.getInstance().getLevelUpEffectsEnabled()) {
            return;
        }

        if ((event.getSkillLevel() % Config.getInstance().getLevelUpEffectsTier()) == 0) {
            skill.celebrateLevelUp(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerXp(McMMOPlayerXpGainEvent event) {
        ScoreboardManager.handleXp(event.getPlayer(), event.getSkill());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAbility(McMMOPlayerAbilityActivateEvent event) {
        ScoreboardManager.cooldownUpdate(event.getPlayer(), event.getSkill());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerXpGain(McMMOPlayerXpGainEvent event) {
        SkillType skillType = event.getSkill();
        int threshold = ExperienceConfig.getInstance().getDiminishedReturnsThreshold(skillType);

        if (threshold <= 0 || !ExperienceConfig.getInstance().getDiminishedReturnsEnabled()) {
            // Diminished returns is turned off
            return;
        }

        final float rawXp = event.getRawXpGained();
        if (rawXp < 0) {
            // Don't calculate for XP subtraction
            return;
        }

        Player player = event.getPlayer();
        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);

        if (skillType.isChildSkill()) {
            return;
        }

        float modifiedThreshold = (float) (threshold / skillType.getXpModifier() * ExperienceConfig.getInstance().getExperienceGainsGlobalMultiplier());
        float difference = (mcMMOPlayer.getProfile().getRegisteredXpGain(skillType) - modifiedThreshold) / modifiedThreshold;

        if (difference > 0) {
//            System.out.println("Total XP Earned: " + mcMMOPlayer.getProfile().getRegisteredXpGain(skillType) + " / Threshold value: " + threshold);
//            System.out.println(difference * 100 + "% over the threshold!");
//            System.out.println("Previous: " + event.getRawXpGained());
//            System.out.println("Adjusted XP " + (event.getRawXpGained() - (event.getRawXpGained() * difference)));
            float newValue = rawXp - (rawXp * difference);

            if (newValue > 0) {
                event.setRawXpGained(newValue);
            } else {
                event.setCancelled(true);
            }
        }
    }
}
