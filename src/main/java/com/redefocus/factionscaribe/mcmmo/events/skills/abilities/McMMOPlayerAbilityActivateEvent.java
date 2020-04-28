package com.redefocus.factionscaribe.mcmmo.events.skills.abilities;

import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class McMMOPlayerAbilityActivateEvent extends McMMOPlayerAbilityEvent implements Cancellable {

    private boolean cancelled;

    public McMMOPlayerAbilityActivateEvent(Player player, SkillType skill) {
        super(player, skill);
        cancelled = false;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean value) {
        this.cancelled = value;
    }
}
