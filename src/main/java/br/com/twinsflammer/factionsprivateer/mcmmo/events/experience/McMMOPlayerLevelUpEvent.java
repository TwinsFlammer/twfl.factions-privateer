package br.com.twinsflammer.factionsprivateer.mcmmo.events.experience;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.XPGainReason;
import org.bukkit.entity.Player;

/**
 * Called when a user levels up in a skill
 */
public class McMMOPlayerLevelUpEvent extends McMMOPlayerLevelChangeEvent {

    private int levelsGained;

    @Deprecated
    public McMMOPlayerLevelUpEvent(Player player, SkillType skill) {
        super(player, skill, XPGainReason.UNKNOWN);
        this.levelsGained = 1;
    }

    @Deprecated
    public McMMOPlayerLevelUpEvent(Player player, SkillType skill, int levelsGained) {
        super(player, skill, XPGainReason.UNKNOWN);
        this.levelsGained = levelsGained;
    }

    public McMMOPlayerLevelUpEvent(Player player, SkillType skill, XPGainReason xpGainReason) {
        super(player, skill, xpGainReason);
        this.levelsGained = 1;
    }

    public McMMOPlayerLevelUpEvent(Player player, SkillType skill, int levelsGained, XPGainReason xpGainReason) {
        super(player, skill, xpGainReason);
        this.levelsGained = levelsGained;
    }

    /**
     * @param levelsGained Set the number of levels gained in this event
     */
    public void setLevelsGained(int levelsGained) {
        this.levelsGained = levelsGained;
    }

    /**
     * @return The number of levels gained in this event
     */
    public int getLevelsGained() {
        return levelsGained;
    }
}
