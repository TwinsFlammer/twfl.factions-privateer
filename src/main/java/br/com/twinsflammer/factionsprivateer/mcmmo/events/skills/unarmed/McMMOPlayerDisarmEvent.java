package br.com.twinsflammer.factionsprivateer.mcmmo.events.skills.unarmed;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.skills.McMMOPlayerSkillEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class McMMOPlayerDisarmEvent extends McMMOPlayerSkillEvent implements Cancellable {

    private boolean cancelled;
    private Player defender;

    public McMMOPlayerDisarmEvent(Player defender) {
        super(defender, SkillType.UNARMED);
        this.defender = defender;
    }

    public Player getDefender() {
        return defender;
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
}
