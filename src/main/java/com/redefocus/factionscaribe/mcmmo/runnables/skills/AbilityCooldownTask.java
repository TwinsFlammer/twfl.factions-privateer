package com.redefocus.factionscaribe.mcmmo.runnables.skills;

import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.AbilityType;
import org.bukkit.scheduler.BukkitRunnable;

public class AbilityCooldownTask extends BukkitRunnable {

    private McMMOPlayer mcMMOPlayer;
    private AbilityType ability;

    public AbilityCooldownTask(McMMOPlayer mcMMOPlayer, AbilityType ability) {
        this.mcMMOPlayer = mcMMOPlayer;
        this.ability = ability;
    }

    @Override
    public void run() {
        if (mcMMOPlayer.getAbilityInformed(ability)) {
            return;
        }

        mcMMOPlayer.setAbilityInformed(ability, true);
        mcMMOPlayer.getPlayer().sendMessage(ability.getAbilityRefresh());
    }
}
