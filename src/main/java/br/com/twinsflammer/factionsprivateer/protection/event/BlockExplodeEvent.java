package br.com.twinsflammer.factionsprivateer.protection.event;

import br.com.twinsflammer.api.spigot.event.TwinsEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

import java.util.List;

/**
 * @author SrGutyerrez
 */
@RequiredArgsConstructor
public class BlockExplodeEvent extends TwinsEvent implements Cancellable {
    @Getter
    private final Entity entity;
    private final List<Block> blocks;

    private Boolean cancelled = false;

    public List<Block> blockList() {
        return this.blocks;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
