package br.com.twinsflammer.factionscaribe.mcmmo.skills;

import br.com.twinsflammer.api.spigot.util.action.data.CustomAction;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.XPGainReason;
import br.com.twinsflammer.factionscaribe.mcmmo.util.skills.PerksUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public abstract class SkillManager {

    protected McMMOPlayer mcMMOPlayer;
    protected int activationChance;
    protected SkillType skill;

    public SkillManager(McMMOPlayer mcMMOPlayer, SkillType skill) {
        this.mcMMOPlayer = mcMMOPlayer;
        this.activationChance = PerksUtils.handleLuckyPerks(mcMMOPlayer.getPlayer(), skill);
        this.skill = skill;
    }

    public Player getPlayer() {
        return mcMMOPlayer.getPlayer();
    }

    public int getSkillLevel() {
        return mcMMOPlayer.getSkillLevel(skill);
    }

    public void applyXpGain(float xp, XPGainReason xpGainReason) {
        mcMMOPlayer.beginXpGain(skill, xp, xpGainReason);

        DecimalFormat decimalFormat = new DecimalFormat("###,###");

        new CustomAction()
                .text(
                        String.format(
                                "§c%s %d (%d/%d) +%s XP",
                                skill.getName(),
                                mcMMOPlayer.getSkillLevel(skill),
                                mcMMOPlayer.getSkillXpLevel(skill),
                                mcMMOPlayer.getXpToLevel(skill),
                                decimalFormat.format(xp)
                        )
                )
                .spigot()
                .send(this.getPlayer());
    }

    public XPGainReason getXPGainReason(LivingEntity target, Entity damager) {
        return (damager instanceof Player && target instanceof Player) ? XPGainReason.PVP : XPGainReason.PVE;
    }
}
