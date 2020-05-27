package br.com.twinsflammer.factionsprivateer.spawner.stacker.listener;

import br.com.twinsflammer.api.spigot.inventory.item.CustomItem;
import br.com.twinsflammer.factionsprivateer.util.BlockUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SrGutyerrez
 */
public class EntityDeathListener implements Listener {
    private final Material[] BLACKLISTED_TO_DROP = {
            Material.RED_ROSE
    };

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();

        Entity killer = livingEntity.getKiller();

        if (!Arrays.asList(SpawnerSpawnListener.ALLOWED_ENTITY_TYPES).contains(livingEntity.getType()))
            return;

        Integer amount = livingEntity.hasMetadata(SpawnerSpawnListener.STACK_METADATA) ? livingEntity.getMetadata(SpawnerSpawnListener.STACK_METADATA).get(0).asInt() : 1;

        EntityType entityType = livingEntity.getType();
        Location location = livingEntity.getLocation();
        World world = location.getWorld();

        Integer multiplier = this.getSpawnerMultiplier(
                entityType,
                location
        );

        if (multiplier > amount) multiplier = amount;

        Integer newAmount = amount - multiplier;

        if (amount > multiplier && newAmount > 0) {
            livingEntity.remove();

            Entity newEntity = world.spawnEntity(location, entityType);

            SpawnerSpawnListener.updateAmount(
                    newEntity,
                    amount - multiplier
            );
        }

        if (killer != null && killer.getType() == EntityType.PLAYER) {
            Player player = (Player) killer;

            ItemStack itemStack = player.getItemInHand();

            if (itemStack.getType().name().endsWith("_SWORD")) {
                Integer lootingEnchantmentLevel = itemStack.containsEnchantment(Enchantment.LOOT_BONUS_MOBS) ? itemStack.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) : 0;

                if (lootingEnchantmentLevel != 0) {
                    multiplier = multiplier * lootingEnchantmentLevel;
                }
            }
        }

        Integer dropMultiplier = multiplier == 0 ? 1 : multiplier;

        List<Material> drops = event.getDrops()
                .stream()
                .map(ItemStack::getType)
                .distinct()
                .collect(Collectors.toList());

        event.getDrops().clear();

        drops.forEach(material -> {
            if (!Arrays.asList(this.BLACKLISTED_TO_DROP).contains(material)) {
                ItemStack itemStack = new CustomItem(material)
                        .amount(dropMultiplier)
                        .build();

                event.getDrops().add(itemStack);
            }
        });
        event.setDroppedExp(event.getDroppedExp() * dropMultiplier);
    }

    private Integer getSpawnerMultiplier(EntityType entityType, Location location) {
        return (int) BlockUtil.getNearbyBlocks(location, 32)
                .stream()
                .filter(block -> block.getType() == Material.MOB_SPAWNER)
                .filter(block -> block.getY() >= location.getY())
                .filter(block -> {
                    CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();

                    return creatureSpawner.getSpawnedType().equals(entityType);
                })
                .count();
    }
}
