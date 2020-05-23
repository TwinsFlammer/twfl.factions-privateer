package br.com.twinsflammer.factionsprivateer.spawner.manager;

import br.com.twinsflammer.api.spigot.util.NBTTag;
import br.com.twinsflammer.common.shared.Common;
import br.com.twinsflammer.factionsprivateer.spawner.item.Spawner;
import com.google.common.collect.Maps;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author SrGutyerrez
 */
public class SpawnerManager {
    private static HashMap<Location, Long> PLACED_SPAWNERS = Maps.newHashMap();

    public SpawnerManager() {
        Common.getInstance().getScheduler().scheduleAtFixedRate(
                () -> {
                    Iterator<Map.Entry<Location, Long>> iterator = SpawnerManager.PLACED_SPAWNERS.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Map.Entry<Location, Long> entry = iterator.next();

                        Long placedTime = entry.getValue();
                        Long maxCacheTime = placedTime + TimeUnit.SECONDS.toMillis(5);

                        if (System.currentTimeMillis() > maxCacheTime)
                            iterator.remove();
                    }
                },
                0,
                20L,
                TimeUnit.SECONDS
        );
    }

    public static Long getTimeToBreak(Location location) {
        return SpawnerManager.PLACED_SPAWNERS.get(location) + TimeUnit.SECONDS.toMillis(5);
    }

    public static ItemStack createSpawner(EntityType entityType, Integer amount) {
        ItemStack spawner = new Spawner(entityType)
                .amount(amount)
                .build();

        NBTTagCompound nbtTagCompound = NBTTag.getNBTTag(spawner);

        nbtTagCompound.setString(Spawner.NBT_TAG, entityType.toString());

        spawner = NBTTag.setNBTTag(spawner, nbtTagCompound);

        return spawner;
    }

    public static ItemStack getSpawner(Block block) {
        CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();

        EntityType entityType = creatureSpawner.getSpawnedType();

        return SpawnerManager.createSpawner(
                entityType,
                1
        );
    }

    public static Boolean canBreakSpawner(Location location) {
        return System.currentTimeMillis() > SpawnerManager.getTimeToBreak(location);
    }
}
