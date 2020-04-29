package com.redefocus.factionscaribe.mcmmo.skills;

import com.redefocus.api.spigot.util.action.data.CustomAction;
import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.XPGainReason;
import com.redefocus.factionscaribe.mcmmo.util.skills.PerksUtils;
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
                                "§cMineração %d (%d/%d) +%s XP",
                                mcMMOPlayer.getSkillLevel(SkillType.MINING),
                                mcMMOPlayer.getSkillXpLevel(SkillType.MINING),
                                mcMMOPlayer.getXpToLevel(SkillType.MINING),
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
