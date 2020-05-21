package br.com.twinsflammer.factionsprivateer.mcmmo.events.skills.abilities;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import org.bukkit.entity.Player;

public class McMMOPlayerAbilityDeactivateEvent extends McMMOPlayerAbilityEvent {

    public McMMOPlayerAbilityDeactivateEvent(Player player, SkillType skill) {
        super(player, skill);
    }
}
