package br.com.twinsflammer.factionsprivateer.mcmmo.events.hardcore;

import java.util.HashMap;
import org.bukkit.entity.Player;

public class McMMOPlayerVampirismEvent extends McMMOPlayerDeathPenaltyEvent {

    private boolean isVictim;

    public McMMOPlayerVampirismEvent(Player player, boolean isVictim, HashMap<String, Integer> levelChanged, HashMap<String, Float> experienceChanged) {
        super(player, levelChanged, experienceChanged);
        this.isVictim = isVictim;
    }

    public boolean isVictim() {
        return isVictim;
    }
}
