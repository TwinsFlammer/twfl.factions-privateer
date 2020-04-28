package com.redefocus.factionscaribe.mcmmo.events.experience;

import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.XPGainReason;
import org.bukkit.entity.Player;

/**
 * Called when a user levels change
 */
public abstract class McMMOPlayerLevelChangeEvent extends McMMOPlayerExperienceEvent {

    @Deprecated
    public McMMOPlayerLevelChangeEvent(Player player, SkillType skill) {
        super(player, skill, XPGainReason.UNKNOWN);
    }

    public McMMOPlayerLevelChangeEvent(Player player, SkillType skill, XPGainReason xpGainReason) {
        super(player, skill, xpGainReason);
    }
}
