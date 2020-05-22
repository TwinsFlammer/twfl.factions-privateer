package br.com.twinsflammer.factionsprivateer.mcmmo.runnables;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.BlockUtils;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PistonTrackerTask extends BukkitRunnable {

    private List<Block> blocks;
    private BlockFace direction;
    private Block futureEmptyBlock;

    public PistonTrackerTask(List<Block> blocks, BlockFace direction, Block futureEmptyBlock) {
        this.blocks = blocks;
        this.direction = direction;
        this.futureEmptyBlock = futureEmptyBlock;
    }

    @Override
    public void run() {
        // Check to see if futureEmptyBlock is empty - if it isn't; the blocks didn't move
        if (!BlockUtils.isPistonPiece(futureEmptyBlock.getState())) {
            return;
        }

        if (mcMMO.getPlaceStore().isTrue(futureEmptyBlock)) {
            mcMMO.getPlaceStore().setFalse(futureEmptyBlock);
        }

        for (Block b : blocks) {
            Block nextBlock = b.getRelative(direction);

            if (nextBlock.hasMetadata(mcMMO.blockMetadataKey)) {
                mcMMO.getPlaceStore().setTrue(nextBlock);
                nextBlock.removeMetadata(mcMMO.blockMetadataKey, FactionsPrivateer.getInstance());
            } else if (mcMMO.getPlaceStore().isTrue(nextBlock)) {
                // Block doesn't have metadatakey but isTrue - set it to false
                mcMMO.getPlaceStore().setFalse(nextBlock);
            }
        }
    }
}
