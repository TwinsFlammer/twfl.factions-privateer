package br.com.twinsflammer.factionscaribe.mcmmo.events.experience;

import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.XPGainReason;
import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Generic event for mcMMO experience events.
 */
public abstract class McMMOPlayerExperienceEvent extends PlayerEvent implements Cancellable {

    private boolean cancelled;
    protected SkillType skill;
    protected int skillLevel;
    protected XPGainReason xpGainReason;

    @Deprecated
    protected McMMOPlayerExperienceEvent(Player player, SkillType skill) {
        super(player);
        this.skill = skill;
        this.skillLevel = UserManager.getPlayer(player).getSkillLevel(skill);
        this.xpGainReason = XPGainReason.UNKNOWN;
    }

    protected McMMOPlayerExperienceEvent(Player player, SkillType skill, XPGainReason xpGainReason) {
        super(player);
        this.skill = skill;
        this.skillLevel = UserManager.getPlayer(player).getSkillLevel(skill);
        this.xpGainReason = xpGainReason;
    }

    /**
     * @return The skill involved in this event
     */
    public SkillType getSkill() {
        return skill;
    }

    /**
     * @return The skill level of the skill involved in this event
     */
    public int getSkillLevel() {
        return skillLevel;
    }

    /**
     * @return The combat type involved in this event
     */
    public XPGainReason getXpGainReason() {
        return xpGainReason;
    }

    /**
     * Following are required for Cancellable *
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
