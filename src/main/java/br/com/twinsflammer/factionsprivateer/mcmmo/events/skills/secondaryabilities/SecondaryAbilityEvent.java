package br.com.twinsflammer.factionsprivateer.mcmmo.events.skills.secondaryabilities;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SecondaryAbility;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.skills.McMMOPlayerSkillEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class SecondaryAbilityEvent extends McMMOPlayerSkillEvent implements Cancellable {

    private SecondaryAbility secondaryAbility;
    private boolean cancelled;

    public SecondaryAbilityEvent(Player player, SecondaryAbility secondaryAbility) {
        super(player, SkillType.bySecondaryAbility(secondaryAbility));
        this.secondaryAbility = secondaryAbility;
        cancelled = false;
    }

    /**
     * Gets the SecondaryAbility involved in the event
     *
     * @return the SecondaryAbility
     */
    public SecondaryAbility getSecondaryAbility() {
        return secondaryAbility;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean newValue) {
        this.cancelled = newValue;
    }
}
