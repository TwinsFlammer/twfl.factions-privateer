package br.com.twinsflammer.factionsprivateer.mcmmo.events.fake;

import org.bukkit.block.Block;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;

public class FakeBrewEvent extends BrewEvent {

    public FakeBrewEvent(Block brewer, BrewerInventory contents) {
        super(brewer, contents);
    }
}
