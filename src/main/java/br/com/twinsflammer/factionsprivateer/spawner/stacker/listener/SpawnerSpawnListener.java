package br.com.twinsflammer.factionsprivateer.spawner.stacker.listener;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.util.EntityUtil;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SrGutyerrez
 */
public class SpawnerSpawnListener implements Listener {
    public static final String STACK_METADATA = "STACKED_AMOUNT";

    public static final Integer STACK_LIMIT = 300;

    public static final EntityType[] ALLOWED_ENTITY_TYPES = {
            EntityType.IRON_GOLEM,
            EntityType.BLAZE,
            EntityType.PIG_ZOMBIE,
            EntityType.SKELETON,
            EntityType.ZOMBIE,
            EntityType.SPIDER,
            EntityType.COW
    };

    private final Double RANGE = 32.0;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onSpawn(SpawnerSpawnEvent event) {
        CreatureSpawner creatureSpawner = event.getSpawner();

        Location location = creatureSpawner.getLocation();

        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(location));

        if (factionAt == null || this.isDefaultFaction(factionAt)) {
            event.setCancelled(true);
            return;
        }

        Entity entity = event.getEntity();

        if (!Arrays.asList(SpawnerSpawnListener.ALLOWED_ENTITY_TYPES).contains(entity.getType())) {
            event.setCancelled(true);
            return;
        }

        List<Entity> entities = entity.getNearbyEntities(this.RANGE, this.RANGE, this.RANGE)
                .stream()
                .filter(entity1 -> !entity1.isDead())
                .filter(entity1 -> !entity1.hasMetadata("NPC"))
                .filter(entity1 -> entity1.getType().equals(entity.getType()))
                .collect(Collectors.toList());

        if (entities.isEmpty()) {
            this.updateAmount(entity);
        } else {
            entity.remove();

            Entity receiver = entities.get(0);

            this.updateAmount(receiver);
        }
    }

    protected void updateAmount(Entity entity) {
        Integer currentAmount = entity.hasMetadata(SpawnerSpawnListener.STACK_METADATA) ? entity.getMetadata(this.STACK_METADATA).get(0).asInt() : 0;

        currentAmount++;

        if (currentAmount > SpawnerSpawnListener.STACK_LIMIT) return;

        System.out.println(currentAmount);

        entity.setMetadata(SpawnerSpawnListener.STACK_METADATA, new FixedMetadataValue(FactionsPrivateer.getInstance(), currentAmount));

        entity.setCustomName(
                String.format(
                        "§a%dx %s",
                        currentAmount,
                        EntityUtil.translate(entity.getName())
                )
        );

        System.out.println(entity.getCustomName());
    }

    protected Boolean isDefaultFaction(Faction faction) {
        return faction.getId().equals(Factions.ID_NONE)
                || faction.getId().equals(Factions.ID_WARZONE)
                || faction.getId().equals(Factions.ID_SAFEZONE);
    }
}
