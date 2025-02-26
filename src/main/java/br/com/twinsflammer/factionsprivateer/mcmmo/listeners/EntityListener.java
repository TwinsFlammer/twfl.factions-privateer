package br.com.twinsflammer.factionsprivateer.mcmmo.listeners;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.AdvancedConfig;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SecondaryAbility;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.fake.FakeEntityDamageByEntityEvent;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.fake.FakeEntityDamageEvent;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.fake.FakeEntityTameEvent;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.mcmmo.party.PartyManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.runnables.skills.BleedTimerTask;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.acrobatics.AcrobaticsManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.archery.Archery;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.fishing.Fishing;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.herbalism.Herbalism;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.mining.BlastMining;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.mining.MiningManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.taming.Taming;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.taming.TamingManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.BlockUtils;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Misc;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Permissions;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.skills.CombatUtils;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.skills.SkillUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class EntityListener implements Listener {

    private final mcMMO plugin;

    public EntityListener(final mcMMO plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityShootBow(EntityShootBowEvent event) {
        Entity projectile = event.getProjectile();

        if (!(projectile instanceof Arrow)) {
            return;
        }

        ItemStack bow = event.getBow();

        if (bow != null && bow.containsEnchantment(Enchantment.ARROW_INFINITE)) {
            projectile.setMetadata(mcMMO.infiniteArrowKey, mcMMO.metadataValue);
        }

        projectile.setMetadata(mcMMO.bowForceKey, new FixedMetadataValue(FactionsPrivateer.getInstance(), Math.min(event.getForce() * AdvancedConfig.getInstance().getForceMultiplier(), 1.0)));
        projectile.setMetadata(mcMMO.arrowDistanceKey, new FixedMetadataValue(FactionsPrivateer.getInstance(), projectile.getLocation()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();

        if (!(projectile instanceof Arrow) || projectile.hasMetadata(mcMMO.bowForceKey)) {
            return;
        }

        projectile.setMetadata(mcMMO.bowForceKey, new FixedMetadataValue(FactionsPrivateer.getInstance(), 1.0));
        projectile.setMetadata(mcMMO.arrowDistanceKey, new FixedMetadataValue(FactionsPrivateer.getInstance(), projectile.getLocation()));
    }

    /**
     * Monitor EntityChangeBlock events.
     *
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Block block = event.getBlock();

        // When the event is fired for the falling block that changes back to a
        // normal block
        // event.getBlock().getType() returns AIR
        if (!BlockUtils.shouldBeWatched(block.getState()) && block.getType() != Material.AIR) {
            return;
        }

        Entity entity = event.getEntity();

        if (entity instanceof FallingBlock || entity instanceof Enderman) {
            boolean isTracked = entity.hasMetadata(mcMMO.entityMetadataKey);

            if (mcMMO.getPlaceStore().isTrue(block) && !isTracked) {
                mcMMO.getPlaceStore().setFalse(block);
                entity.setMetadata(mcMMO.entityMetadataKey, mcMMO.metadataValue);
            } else if (isTracked) {
                mcMMO.getPlaceStore().setTrue(block);
            }
        } else if ((block.getType() == Material.REDSTONE_ORE || block.getType() == Material.GLOWING_REDSTONE_ORE) && (event.getTo() == Material.REDSTONE_ORE || event.getTo() == Material.GLOWING_REDSTONE_ORE)) {
            return;
        } else if (mcMMO.getPlaceStore().isTrue(block)) {
            mcMMO.getPlaceStore().setFalse(block);
        }
    }

    /**
     * Handle EntityDamageByEntity events that involve modifying the event.
     *
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event instanceof FakeEntityDamageByEntityEvent) {
            return;
        }

        double damage = event.getFinalDamage();
        Entity defender = event.getEntity();
        Entity attacker = event.getDamager();

        if (damage <= 0) {
            if (defender instanceof Player && attacker instanceof Player) {
                Player defendingPlayer = (Player) defender;
                Player attackingPlayer = (Player) attacker;
                if (event.getDamage(DamageModifier.ABSORPTION) > 0) {
                    if ((PartyManager.inSameParty(defendingPlayer, attackingPlayer) || PartyManager.areAllies(defendingPlayer, attackingPlayer)) && !(Permissions.friendlyFire(attackingPlayer) && Permissions.friendlyFire(defendingPlayer))) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
            return;
        }

        if (defender.hasMetadata(mcMMO.customDamageKey)) {
            defender.removeMetadata(mcMMO.customDamageKey, FactionsPrivateer.getInstance());
            return;
        }

        if (Misc.isNPCEntity(defender) || !defender.isValid() || !(defender instanceof LivingEntity)) {
            return;
        }

        LivingEntity target = (LivingEntity) defender;

        if (CombatUtils.isInvincible(target, damage)) {
            return;
        }

        if (Misc.isNPCEntity(attacker)) {
            return;
        }

        if (attacker instanceof Projectile) {
            ProjectileSource projectileSource = ((Projectile) attacker).getShooter();

            if (projectileSource instanceof LivingEntity) {
                attacker = (LivingEntity) projectileSource;
            }
        } else if (attacker instanceof Tameable) {
            AnimalTamer animalTamer = ((Tameable) attacker).getOwner();

            if (animalTamer != null && ((OfflinePlayer) animalTamer).isOnline()) {
                attacker = (Entity) animalTamer;
            }
        } else if (attacker instanceof TNTPrimed && defender instanceof Player) {
            if (BlastMining.processBlastMiningExplosion(event, (TNTPrimed) attacker, (Player) defender)) {
                return;
            }
        }

        if (defender instanceof Player && attacker instanceof Player) {
            Player defendingPlayer = (Player) defender;
            Player attackingPlayer = (Player) attacker;

            if (!UserManager.hasPlayerDataKey(defendingPlayer) || !UserManager.hasPlayerDataKey(attackingPlayer)) {
                return;
            }

            // We want to make sure we're not gaining XP or applying abilities
            // when we hit ourselves
            if (defendingPlayer.equals(attackingPlayer)) {
                return;
            }

            if ((PartyManager.inSameParty(defendingPlayer, attackingPlayer) || PartyManager.areAllies(defendingPlayer, attackingPlayer)) && !(Permissions.friendlyFire(attackingPlayer) && Permissions.friendlyFire(defendingPlayer))) {
                event.setCancelled(true);
                return;
            }
        }

        CombatUtils.processCombatAttack(event, attacker, target);
        CombatUtils.handleHealthbars(attacker, target, event.getFinalDamage());
    }

    /**
     * Handle EntityDamage events that involve modifying the event.
     *
     * @param event The event to modify
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event instanceof FakeEntityDamageEvent) {
            return;
        }

        double damage = event.getFinalDamage();

        if (damage <= 0) {
            return;
        }

        Entity entity = event.getEntity();

        if (entity.hasMetadata(mcMMO.customDamageKey)) {
            entity.removeMetadata(mcMMO.customDamageKey, FactionsPrivateer.getInstance());
            return;
        }

        if (Misc.isNPCEntity(entity) || !entity.isValid() || !(entity instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = (LivingEntity) entity;

        if (CombatUtils.isInvincible(livingEntity, damage)) {
            return;
        }

        DamageCause cause = event.getCause();

        if (livingEntity instanceof Player) {
            Player player = (Player) entity;

            if (!UserManager.hasPlayerDataKey(player)) {
                return;
            }

            McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);

            /* Check for invincibility */
            if (mcMMOPlayer.getGodMode()) {
                event.setCancelled(true);
                return;
            }

            switch (cause) {
                case FALL:
                    if (!SkillUtils.cooldownExpired((long) mcMMOPlayer.getTeleportATS(), Config.getInstance().getXPAfterTeleportCooldown())) {
                        return;
                    }

                    AcrobaticsManager acrobaticsManager = mcMMOPlayer.getAcrobaticsManager();

                    if (acrobaticsManager.canRoll()) {
                        event.setDamage(acrobaticsManager.rollCheck(event.getDamage()));

                        if (event.getFinalDamage() == 0) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                    break;

                default:
                    break;
            }

            if (event.getFinalDamage() >= 1) {
                mcMMOPlayer.actualizeRecentlyHurt();
            }
        } else if (livingEntity instanceof Tameable) {
            Tameable pet = (Tameable) livingEntity;
            AnimalTamer owner = pet.getOwner();

            if (Taming.canPreventDamage(pet, owner)) {
                Player player = (Player) owner;
                Wolf wolf = (Wolf) pet;

                TamingManager tamingManager = UserManager.getPlayer(player).getTamingManager();

                switch (cause) {
                    case CONTACT:
                    case FIRE:
                    case LAVA:
                        if (tamingManager.canUseEnvironmentallyAware()) {
                            tamingManager.processEnvironmentallyAware(wolf, event.getDamage());
                        }
                        return;

                    case FALL:
                        if (tamingManager.canUseEnvironmentallyAware()) {
                            event.setCancelled(true);
                        }
                        return;

                    case ENTITY_ATTACK:
                    case PROJECTILE:
                        if (tamingManager.canUseThickFur()) {
                            event.setDamage(Taming.processThickFur(wolf, event.getDamage()));

                            if (event.getFinalDamage() == 0) {
                                event.setCancelled(true);
                            }
                        }
                        return;

                    case FIRE_TICK:
                        if (tamingManager.canUseThickFur()) {
                            Taming.processThickFurFire(wolf);
                        }
                        return;

                    case MAGIC:
                    case POISON:
                    case WITHER:
                        if (tamingManager.canUseHolyHound()) {
                            Taming.processHolyHound(wolf, event.getDamage());
                        }
                        return;

                    case BLOCK_EXPLOSION:
                    case ENTITY_EXPLOSION:
                    case LIGHTNING:
                        if (tamingManager.canUseShockProof()) {
                            event.setDamage(Taming.processShockProof(wolf, event.getDamage()));

                            if (event.getFinalDamage() == 0) {
                                event.setCancelled(true);
                            }
                        }
                        return;

                    default:
                        return;
                }
            }
        }
    }

    /**
     * Monitor EntityDeath events.
     *
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeathLowest(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (Misc.isNPCEntity(entity)) {
            return;
        }

        if (entity.hasMetadata(mcMMO.customNameKey)) {
            entity.setCustomName(entity.getMetadata(mcMMO.customNameKey).get(0).asString());
            entity.removeMetadata(mcMMO.customNameKey, FactionsPrivateer.getInstance());
        }

        if (entity.hasMetadata(mcMMO.customVisibleKey)) {
            entity.setCustomNameVisible(entity.getMetadata(mcMMO.customVisibleKey).get(0).asBoolean());
            entity.removeMetadata(mcMMO.customVisibleKey, FactionsPrivateer.getInstance());
        }
    }

    /**
     * Monitor EntityDeath events.
     *
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (Misc.isNPCEntity(entity)) {
            return;
        }

        BleedTimerTask.remove(entity);
        Archery.arrowRetrievalCheck(entity);
    }

    /**
     * Monitor CreatureSpawn events.
     *
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();

        switch (event.getSpawnReason()) {
            case NETHER_PORTAL:
            case SPAWNER:
            case SPAWNER_EGG:
                entity.setMetadata(mcMMO.entityMetadataKey, mcMMO.metadataValue);

                Entity passenger = entity.getPassenger();

                if (passenger != null) {
                    passenger.setMetadata(mcMMO.entityMetadataKey, mcMMO.metadataValue);
                }
                return;

            case BREEDING:
                entity.setMetadata(mcMMO.bredMetadataKey, mcMMO.metadataValue);
                return;

            default:
                return;
        }
    }

    /**
     * Handle ExplosionPrime events that involve modifying the event.
     *
     * @param event The event to modify
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof TNTPrimed) || !entity.hasMetadata(mcMMO.tntMetadataKey)) {
            return;
        }

        // We can make this assumption because we (should) be the only ones
        // using this exact metadata
        Player player = FactionsPrivateer.getInstance().getServer().getPlayerExact(entity.getMetadata(mcMMO.tntMetadataKey).get(0).asString());

        if (!UserManager.hasPlayerDataKey(player)) {
            return;
        }

        MiningManager miningManager = UserManager.getPlayer(player).getMiningManager();

        if (miningManager.canUseBiggerBombs()) {
            event.setRadius(miningManager.biggerBombs(event.getRadius()));
        }
    }

    /**
     * Handle EntityExplode events that involve modifying the event.
     *
     * @param event The event to modify
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEnitityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof TNTPrimed) || !entity.hasMetadata(mcMMO.tntMetadataKey)) {
            return;
        }

        // We can make this assumption because we (should) be the only ones
        // using this exact metadata
        Player player = FactionsPrivateer.getInstance().getServer().getPlayerExact(entity.getMetadata(mcMMO.tntMetadataKey).get(0).asString());

        if (!UserManager.hasPlayerDataKey(player)) {
            return;
        }

        MiningManager miningManager = UserManager.getPlayer(player).getMiningManager();

        if (miningManager.canUseBlastMining()) {
            miningManager.blastMiningDropProcessing(event.getYield(), event.blockList());
            event.setYield(0);
        }
    }

    /**
     * Handle EntityExplode events that involve modifying the event.
     *
     * @param event The event to modify
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityExplodeMonitor(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof TNTPrimed) || !entity.hasMetadata(mcMMO.tntsafeMetadataKey)) {
            return;
        }

        event.blockList().clear();
    }

    /**
     * Handle FoodLevelChange events that involve modifying the event.
     *
     * @param event The event to modify
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;

        if (!UserManager.hasPlayerDataKey(player)) {
            return;
        }

        int currentFoodLevel = player.getFoodLevel();
        int newFoodLevel = event.getFoodLevel();
        int foodChange = newFoodLevel - currentFoodLevel;

        if (foodChange <= 0) {
            return;
        }

        /*
         * Some foods have 3 ranks Some foods have 5 ranks The number of ranks
         * is based on how 'common' the item is We can adjust this quite easily
         * if we find something is giving too much of a bonus
         */
        switch (player.getItemInHand().getType()) {
            case BAKED_POTATO:
            /*
             * RESTORES 3 HUNGER - RESTORES 5 1/2 HUNGER @
             * 1000
             */

            case BREAD:
            /* RESTORES 2 1/2 HUNGER - RESTORES 5 HUNGER @ 1000 */

            case CARROT_ITEM:
            /*
             * RESTORES 2 HUNGER - RESTORES 4 1/2 HUNGER @
             * 1000
             */

            case GOLDEN_CARROT:
            /*
             * RESTORES 3 HUNGER - RESTORES 5 1/2 HUNGER @
             * 1000
             */

            case MUSHROOM_SOUP:
            /*
             * RESTORES 4 HUNGER - RESTORES 6 1/2 HUNGER @
             * 1000
             */

            case PUMPKIN_PIE:
                /*
                 * RESTORES 4 HUNGER - RESTORES 6 1/2 HUNGER @
                 * 1000
                 */

                if (Permissions.secondaryAbilityEnabled(player, SecondaryAbility.FARMERS_DIET)) {
                    event.setFoodLevel(UserManager.getPlayer(player).getHerbalismManager().farmersDiet(Herbalism.farmersDietRankLevel1, newFoodLevel));
                }
                return;

            case COOKIE:
            /* RESTORES 1/2 HUNGER - RESTORES 2 HUNGER @ 1000 */

            case MELON:
            /* RESTORES 1 HUNGER - RESTORES 2 1/2 HUNGER @ 1000 */

            case POISONOUS_POTATO:
            /*
             * RESTORES 1 HUNGER - RESTORES 2 1/2 HUNGER
             * @ 1000
             */

            case POTATO_ITEM:
                /* RESTORES 1/2 HUNGER - RESTORES 2 HUNGER @ 1000 */

                if (Permissions.secondaryAbilityEnabled(player, SecondaryAbility.FARMERS_DIET)) {
                    event.setFoodLevel(UserManager.getPlayer(player).getHerbalismManager().farmersDiet(Herbalism.farmersDietRankLevel2, newFoodLevel));
                }
                return;

            case COOKED_FISH:
                /*
                 * RESTORES 2 1/2 HUNGER - RESTORES 5 HUNGER @
                 * 1000
                 */

                if (Permissions.secondaryAbilityEnabled(player, SecondaryAbility.FISHERMANS_DIET)) {
                    event.setFoodLevel(UserManager.getPlayer(player).getFishingManager().handleFishermanDiet(Fishing.fishermansDietRankLevel1, newFoodLevel));
                }
                return;

            case RAW_FISH:
                /* RESTORES 1 HUNGER - RESTORES 2 1/2 HUNGER @ 1000 */

                if (Permissions.secondaryAbilityEnabled(player, SecondaryAbility.FISHERMANS_DIET)) {
                    event.setFoodLevel(UserManager.getPlayer(player).getFishingManager().handleFishermanDiet(Fishing.fishermansDietRankLevel2, newFoodLevel));
                }
                return;

            default:
                return;
        }
    }

    /**
     * Monitor EntityTame events.
     *
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityTame(EntityTameEvent event) {
        if (event instanceof FakeEntityTameEvent) {
            return;
        }

        Player player = (Player) event.getOwner();
        LivingEntity entity = event.getEntity();

        if (!UserManager.hasPlayerDataKey(player) || Misc.isNPCEntity(entity) || entity.hasMetadata(mcMMO.entityMetadataKey)) {
            return;
        }

        entity.setMetadata(mcMMO.entityMetadataKey, mcMMO.metadataValue);
        UserManager.getPlayer(player).getTamingManager().awardTamingXP(entity);
    }

    /**
     * Handle EntityTarget events.
     *
     * @param event The event to process
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();

        if (!(entity instanceof Tameable) || !(target instanceof Player)) {
            return;
        }

        Player player = (Player) target;
        Tameable tameable = (Tameable) entity;

        if (!UserManager.hasPlayerDataKey(player) || !CombatUtils.isFriendlyPet(player, tameable)) {
            return;
        }

        // isFriendlyPet ensures that the Tameable is: Tamed, owned by a player,
        // and the owner is in the same party
        // So we can make some assumptions here, about our casting and our check
        if (!(Permissions.friendlyFire(player) && Permissions.friendlyFire((Player) tameable.getOwner()))) {
            event.setCancelled(true);
        }
    }

    /**
     * Handle PotionSplash events in order to fix broken Splash Potion of
     * Saturation.
     *
     * @param event The event to process
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPotionSplash(PotionSplashEvent event) {
        for (PotionEffect effect : ((PotionMeta) event.getPotion().getItem().getItemMeta()).getCustomEffects()) {
            if (!effect.getType().equals(PotionEffectType.SATURATION)) {
                return;
            }

            for (LivingEntity entity : event.getAffectedEntities()) {
                int duration = (int) (effect.getDuration() * event.getIntensity(entity));
                entity.addPotionEffect(new PotionEffect(effect.getType(), duration, effect.getAmplifier(), effect.isAmbient()));
            }
        }
    }
}
