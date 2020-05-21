package br.com.twinsflammer.factionsprivateer.mcmmo.events.hardcore;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class McMMOPlayerStatLossEvent extends McMMOPlayerDeathPenaltyEvent {

    public McMMOPlayerStatLossEvent(Player player, HashMap<String, Integer> levelChanged, HashMap<String, Float> experienceChanged) {
        super(player, levelChanged, experienceChanged);
    }
}
