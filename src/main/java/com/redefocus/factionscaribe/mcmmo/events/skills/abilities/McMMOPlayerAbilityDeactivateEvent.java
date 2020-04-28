package com.redefocus.factionscaribe.mcmmo.events.skills.abilities;

import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import org.bukkit.entity.Player;

public class McMMOPlayerAbilityDeactivateEvent extends McMMOPlayerAbilityEvent {

    public McMMOPlayerAbilityDeactivateEvent(Player player, SkillType skill) {
        super(player, skill);
    }
}
