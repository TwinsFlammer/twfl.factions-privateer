package br.com.twinsflammer.factionsprivateer.spawner.manager;

import br.com.twinsflammer.api.spigot.util.NBTTag;
import br.com.twinsflammer.factionsprivateer.spawner.item.Spawner;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/**
 * @author SrGutyerrez
 */
public class SpawnerManager {
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
}
