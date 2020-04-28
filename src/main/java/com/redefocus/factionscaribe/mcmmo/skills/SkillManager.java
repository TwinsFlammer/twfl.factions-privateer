package com.redefocus.factionscaribe.mcmmo.skills;

import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.XPGainReason;
import com.redefocus.factionscaribe.mcmmo.util.skills.PerksUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

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
    }

    public XPGainReason getXPGainReason(LivingEntity target, Entity damager) {
        return (damager instanceof Player && target instanceof Player) ? XPGainReason.PVP : XPGainReason.PVE;
    }
}
