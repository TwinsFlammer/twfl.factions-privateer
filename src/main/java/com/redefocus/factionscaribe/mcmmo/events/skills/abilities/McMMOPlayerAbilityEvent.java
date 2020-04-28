package com.redefocus.factionscaribe.mcmmo.events.skills.abilities;

import com.redefocus.factionscaribe.mcmmo.datatypes.skills.AbilityType;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.events.skills.McMMOPlayerSkillEvent;
import org.bukkit.entity.Player;

public class McMMOPlayerAbilityEvent extends McMMOPlayerSkillEvent {

    private AbilityType ability;

    protected McMMOPlayerAbilityEvent(Player player, SkillType skill) {
        super(player, skill);
        ability = skill.getAbility();
    }

    public AbilityType getAbility() {
        return ability;
    }
}
