package br.com.twinsflammer.factionsprivateer.spawner.stacker.listener;

import br.com.twinsflammer.factionsprivateer.util.BlockUtil;
import org.bukkit.Location;
import org.bukkit.Material;
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

        Integer multiplier = this.getSpawnerMultiplier(
                livingEntity.getType(),
                livingEntity.getLocation()
        );

        if (multiplier > amount) multiplier = amount;

        if (amount > multiplier)
            SpawnerSpawnListener.updateAmount(
                    livingEntity,
                    amount - multiplier
            );

        if (killer != null && killer.getType() == EntityType.PLAYER) {
            Player player = (Player) killer;

            ItemStack itemStack = player.getItemInHand();

            if (itemStack.getType().name().endsWith("_SWORD")) {
                Integer lootingEnchantmentLevel = itemStack.containsEnchantment(Enchantment.LOOT_BONUS_MOBS) ? itemStack.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) : 0;

                if (lootingEnchantmentLevel != 0) {
                    multiplier = multiplier * (int) (lootingEnchantmentLevel > 1 ? lootingEnchantmentLevel / 1.5D : 1);
                }
            }
        }

        Integer dropMultiplier = multiplier == 0 ? 1 : multiplier;

        event.getDrops().forEach(itemStack -> {
            Material material = itemStack.getType();

            if (Arrays.asList(this.BLACKLISTED_TO_DROP).contains(material))
                itemStack.setType(Material.AIR);
            else {
                itemStack.setAmount(
                        itemStack.getAmount() * dropMultiplier
                );
            }
        });
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
