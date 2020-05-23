package br.com.twinsflammer.factionsprivateer.util;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

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

    public static Location getLocation(Location location, BlockFace blockFace) {
        if (blockFace == BlockFace.UP) {
            return location.clone().add(0.5, 1.0, 0.5);
        } else if (blockFace == BlockFace.DOWN) {
            return location.clone().add(0.5, -1.0, 0.5);
        } else if (blockFace == BlockFace.NORTH) {
            return location.clone().add(0.5, 0.0, -0.5);
        } else if (blockFace == BlockFace.EAST) {
            return location.clone().add(1.5, 0.0, 0.5);
        } else if (blockFace == BlockFace.SOUTH) {
            return location.clone().add(0.5, 0.0, 1.5);
        } else if (blockFace == BlockFace.WEST) {
            return location.clone().add(-0.5, 0.0, 0.5);
        }

        return location;
    }

    public static BlockFace getPlayerBlockFaceLooking(Player player, Block block) {
        Float direction = (float) Math.toDegrees(Math.atan2(
                player.getLocation().getBlockX() - block.getX(),
                block.getZ() - player.getLocation().getBlockZ())
        );

        return BlockUtil.getClosestFace(direction);
    }

    private static BlockFace getClosestFace(float direction) {
        direction = direction % 360;

        if (direction < 0)
            direction += 360;

        direction = Math.round(direction / 45);

        switch ((int) direction) {
            case 1:
                return BlockFace.NORTH_WEST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.NORTH_EAST;
            case 4:
                return BlockFace.EAST;
            case 5:
                return BlockFace.SOUTH_EAST;
            case 6:
                return BlockFace.SOUTH;
            case 7:
                return BlockFace.SOUTH_WEST;
            default:
                return BlockFace.WEST;
        }
    }
}
