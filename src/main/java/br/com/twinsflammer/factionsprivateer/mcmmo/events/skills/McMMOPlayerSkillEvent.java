package br.com.twinsflammer.factionsprivateer.mcmmo.events.skills;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Generic event for mcMMO skill handling.
 */
public abstract class McMMOPlayerSkillEvent extends PlayerEvent {

    protected SkillType skill;
    protected int skillLevel;

    protected McMMOPlayerSkillEvent(Player player, SkillType skill) {
        super(player);
        this.skill = skill;
        this.skillLevel = UserManager.getPlayer(player).getSkillLevel(skill);
    }

    /**
     * @return The skill involved in this event
     */
    public SkillType getSkill() {
        return skill;
    }

    /**
     * @return The level of the skill involved in this event
     */
    public int getSkillLevel() {
        return skillLevel;
    }

    /**
     * Rest of file is required boilerplate for custom events *
     */
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
