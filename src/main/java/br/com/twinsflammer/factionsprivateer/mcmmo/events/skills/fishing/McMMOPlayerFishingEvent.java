package br.com.twinsflammer.factionsprivateer.mcmmo.events.skills.fishing;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.skills.McMMOPlayerSkillEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class McMMOPlayerFishingEvent extends McMMOPlayerSkillEvent implements Cancellable {

    private boolean cancelled;

    protected McMMOPlayerFishingEvent(Player player) {
        super(player, SkillType.FISHING);
        cancelled = false;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean newValue) {
        this.cancelled = newValue;
    }
}
