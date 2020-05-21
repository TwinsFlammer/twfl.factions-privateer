package br.com.twinsflammer.factionsprivateer.mcmmo.runnables.skills;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.XPGainReason;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class AwardCombatXpTask extends BukkitRunnable {

    private McMMOPlayer mcMMOPlayer;
    private double baseXp;
    private SkillType skillType;
    private LivingEntity target;
    private XPGainReason xpGainReason;
    private double baseHealth;

    public AwardCombatXpTask(McMMOPlayer mcMMOPlayer, SkillType skillType, double baseXp, LivingEntity target, XPGainReason xpGainReason) {
        this.mcMMOPlayer = mcMMOPlayer;
        this.skillType = skillType;
        this.baseXp = baseXp;
        this.target = target;
        this.xpGainReason = xpGainReason;
        baseHealth = target.getHealth();
    }

    @Override
    public void run() {
        double health = target.getHealth();
        double damage = baseHealth - health;

        // May avoid negative xp, we don't know what other plugins do with the entity health
        if (damage <= 0) {
            return;
        }

        // Don't reward the player for overkills
        if (health < 0) {
            damage += health;
        }

        mcMMOPlayer.beginXpGain(skillType, (int) (damage * baseXp), xpGainReason);
    }
}
