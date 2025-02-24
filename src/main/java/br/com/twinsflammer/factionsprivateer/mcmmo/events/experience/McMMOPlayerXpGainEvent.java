package br.com.twinsflammer.factionsprivateer.mcmmo.events.experience;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.XPGainReason;
import org.bukkit.entity.Player;

/**
 * Called when a player gains XP in a skill
 */
public class McMMOPlayerXpGainEvent extends McMMOPlayerExperienceEvent {

    private float xpGained;

    @Deprecated
    public McMMOPlayerXpGainEvent(Player player, SkillType skill, float xpGained) {
        super(player, skill, XPGainReason.UNKNOWN);
        this.xpGained = xpGained;
    }

    public McMMOPlayerXpGainEvent(Player player, SkillType skill, float xpGained, XPGainReason xpGainReason) {
        super(player, skill, xpGainReason);
        this.xpGained = xpGained;
    }

    /**
     * @return The amount of experience gained in this event
     */
    public float getRawXpGained() {
        return xpGained;
    }

    /**
     * @return int amount of experience gained in this event
     */
    @Deprecated
    public int getXpGained() {
        return (int) xpGained;
    }

    /**
     * @param xpGained int amount of experience gained in this event
     */
    public void setRawXpGained(float xpGained) {
        this.xpGained = xpGained;
    }

    /**
     * @param xpGained int amount of experience gained in this event
     */
    @Deprecated
    public void setXpGained(int xpGained) {
        this.xpGained = xpGained;
    }
}
