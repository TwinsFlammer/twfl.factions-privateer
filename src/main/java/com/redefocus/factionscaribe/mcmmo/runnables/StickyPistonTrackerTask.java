package com.redefocus.factionscaribe.mcmmo.runnables;

import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.mcmmo.util.BlockUtils;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

public class StickyPistonTrackerTask extends BukkitRunnable {

    private BlockFace direction;
    private Block block;
    private Block movedBlock;

    public StickyPistonTrackerTask(BlockFace direction, Block block, Block movedBlock) {
        this.direction = direction;
        this.block = block;
        this.movedBlock = movedBlock;
    }

    @Override
    public void run() {
        if (!mcMMO.getPlaceStore().isTrue(movedBlock.getRelative(direction))) {
            return;
        }

        if (!BlockUtils.isPistonPiece(movedBlock.getState())) {
            // The block didn't move
            return;
        }

        // The sticky piston actually pulled the block so move the PlaceStore data
        mcMMO.getPlaceStore().setFalse(movedBlock.getRelative(direction));
        mcMMO.getPlaceStore().setTrue(movedBlock);
    }
}
