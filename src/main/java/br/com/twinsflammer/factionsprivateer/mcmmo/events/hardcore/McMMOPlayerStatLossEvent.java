package br.com.twinsflammer.factionsprivateer.mcmmo.events.hardcore;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class McMMOPlayerStatLossEvent extends McMMOPlayerDeathPenaltyEvent {

    public McMMOPlayerStatLossEvent(Player player, HashMap<String, Integer> levelChanged, HashMap<String, Float> experienceChanged) {
        super(player, levelChanged, experienceChanged);
    }
}
