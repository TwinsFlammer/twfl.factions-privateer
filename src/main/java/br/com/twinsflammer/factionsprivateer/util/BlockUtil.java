package br.com.twinsflammer.factionsprivateer.util;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

/**
 * @author SrGutyerrez
 */
public class BlockUtil {
    public static List<Block> getNearbyBlocks(Location location, Integer radius) {
        List<Block> blocks = Lists.newArrayList();

        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; ++x)
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; ++y)
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; ++z)
                    blocks.add(
                            location.getWorld().getBlockAt(x, y, z)
                    );

        return blocks;
    }
}
