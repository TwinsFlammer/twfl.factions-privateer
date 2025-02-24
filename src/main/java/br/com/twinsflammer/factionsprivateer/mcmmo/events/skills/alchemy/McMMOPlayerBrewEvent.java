package br.com.twinsflammer.factionsprivateer.mcmmo.events.skills.alchemy;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.skills.McMMOPlayerSkillEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class McMMOPlayerBrewEvent extends McMMOPlayerSkillEvent implements Cancellable {

    private BlockState brewingStand;

    private boolean cancelled;

    public McMMOPlayerBrewEvent(Player player, BlockState brewingStand) {
        super(player, SkillType.ALCHEMY);
        this.brewingStand = brewingStand;
        cancelled = false;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean newValue) {
        this.cancelled = newValue;
    }

    public Block getBrewingStandBlock() {
        return brewingStand.getBlock();
    }

    public BrewingStand getBrewingStand() {
        return (BrewingStand) brewingStand;
    }
}
