package br.com.twinsflammer.factionscaribe.mcmmo.events.experience;

import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.XPGainReason;
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
