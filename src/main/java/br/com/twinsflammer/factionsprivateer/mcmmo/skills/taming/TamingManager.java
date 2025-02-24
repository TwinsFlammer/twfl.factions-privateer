package br.com.twinsflammer.factionsprivateer.mcmmo.skills.taming;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.AdvancedConfig;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SecondaryAbility;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.XPGainReason;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.fake.FakeEntityTameEvent;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.skills.secondaryabilities.SecondaryAbilityWeightedActivationCheckEvent;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.mcmmo.runnables.skills.BleedTimerTask;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.SkillManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Misc;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Permissions;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.StringUtils;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.skills.ParticleEffectUtils;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.skills.SkillUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TamingManager extends SkillManager {

    public TamingManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, SkillType.TAMING);
    }

    private static HashMap<EntityType, List<TrackedTamingEntity>> summonedEntities = new HashMap<EntityType, List<TrackedTamingEntity>>();

    public boolean canUseThickFur() {
        return getSkillLevel() >= Taming.thickFurUnlockLevel && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.THICK_FUR);
    }

    public boolean canUseEnvironmentallyAware() {
        return getSkillLevel() >= Taming.environmentallyAwareUnlockLevel && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.ENVIROMENTALLY_AWARE);
    }

    public boolean canUseShockProof() {
        return getSkillLevel() >= Taming.shockProofUnlockLevel && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.SHOCK_PROOF);
    }

    public boolean canUseHolyHound() {
        return getSkillLevel() >= Taming.holyHoundUnlockLevel && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.HOLY_HOUND);
    }

    public boolean canUseFastFoodService() {
        return getSkillLevel() >= Taming.fastFoodServiceUnlockLevel && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.FAST_FOOD);
    }

    public boolean canUseSharpenedClaws() {
        return getSkillLevel() >= Taming.sharpenedClawsUnlockLevel && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.SHARPENED_CLAWS);
    }

    public boolean canUseGore() {
        return Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.GORE);
    }

    public boolean canUseBeastLore() {
        return Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.BEAST_LORE);
    }

    /**
     * Award XP for taming.
     *
     * @param entity The LivingEntity to award XP for
     */
    public void awardTamingXP(LivingEntity entity) {
        switch (entity.getType()) {
            case HORSE:
                applyXpGain(Taming.horseXp, XPGainReason.PVE);
                return;

            case WOLF:
                applyXpGain(Taming.wolfXp, XPGainReason.PVE);
                return;

            case OCELOT:
                applyXpGain(Taming.ocelotXp, XPGainReason.PVE);
                return;

            default:
                return;
        }
    }

    /**
     * Apply the Fast Food Service ability.
     *
     * @param wolf The wolf using the ability
     * @param damage The damage being absorbed by the wolf
     */
    public void fastFoodService(Wolf wolf, double damage) {
        if (!SkillUtils.activationSuccessful(SecondaryAbility.FAST_FOOD, getPlayer(), Taming.fastFoodServiceActivationChance, activationChance)) {
            return;
        }

        double health = wolf.getHealth();
        double maxHealth = wolf.getMaxHealth();

        if (health < maxHealth) {
            double newHealth = health + damage;
            wolf.setHealth(Math.min(newHealth, maxHealth));
        }
    }

    /**
     * Apply the Gore ability.
     *
     * @param target The LivingEntity to apply Gore on
     * @param damage The initial damage
     */
    public double gore(LivingEntity target, double damage) {
        if (!SkillUtils.activationSuccessful(SecondaryAbility.GORE, getPlayer(), getSkillLevel(), activationChance)) {
            return 0;
        }

        BleedTimerTask.add(target, Taming.goreBleedTicks);

        if (target instanceof Player) {
            ((Player) target).sendMessage(LocaleLoader.getString("Combat.StruckByGore"));
        }

        getPlayer().sendMessage(LocaleLoader.getString("Combat.Gore"));

        damage = (damage * Taming.goreModifier) - damage;
        return damage;
    }

    public double sharpenedClaws() {
        return Taming.sharpenedClawsBonusDamage;
    }

    /**
     * Summon an ocelot to your side.
     */
    public void summonOcelot() {
        if (!Permissions.callOfTheWild(getPlayer(), EntityType.OCELOT)) {
            return;
        }

        callOfTheWild(EntityType.OCELOT, Config.getInstance().getTamingCOTWCost(EntityType.OCELOT));
    }

    /**
     * Summon a wolf to your side.
     */
    public void summonWolf() {
        if (!Permissions.callOfTheWild(getPlayer(), EntityType.WOLF)) {
            return;
        }

        callOfTheWild(EntityType.WOLF, Config.getInstance().getTamingCOTWCost(EntityType.WOLF));
    }

    /**
     * Summon a horse to your side.
     */
    public void summonHorse() {
        if (!Permissions.callOfTheWild(getPlayer(), EntityType.HORSE)) {
            return;
        }

        callOfTheWild(EntityType.HORSE, Config.getInstance().getTamingCOTWCost(EntityType.HORSE));
    }

    /**
     * Handle the Beast Lore ability.
     *
     * @param target The entity to examine
     */
    public void beastLore(LivingEntity target) {
        Player player = getPlayer();
        Tameable beast = (Tameable) target;

        String message = LocaleLoader.getString("Combat.BeastLore") + " ";

        if (beast.isTamed() && beast.getOwner() != null) {
            message = message.concat(LocaleLoader.getString("Combat.BeastLoreOwner", beast.getOwner().getName()) + " ");
        }

        message = message.concat(LocaleLoader.getString("Combat.BeastLoreHealth", target.getHealth(), target.getMaxHealth()));
        player.sendMessage(message);
    }

    public void processEnvironmentallyAware(Wolf wolf, double damage) {
        if (damage > wolf.getHealth()) {
            return;
        }

        Player owner = getPlayer();

        wolf.teleport(owner);
        owner.sendMessage(LocaleLoader.getString("Taming.Listener.Wolf"));
    }

    public void pummel(LivingEntity target, Wolf wolf) {
        double chance = 10 / activationChance;
        SecondaryAbilityWeightedActivationCheckEvent event = new SecondaryAbilityWeightedActivationCheckEvent(getPlayer(), SecondaryAbility.PUMMEL, chance);
        FactionsPrivateer.getInstance().getServer().getPluginManager().callEvent(event);
        if ((event.getChance() * activationChance) <= Misc.getRandom().nextInt(activationChance)) {
            return;
        }

        ParticleEffectUtils.playGreaterImpactEffect(target);
        target.setVelocity(wolf.getLocation().getDirection().normalize().multiply(1.5D));

        if (target instanceof Player) {
            Player defender = (Player) target;

            if (UserManager.getPlayer(defender).useChatNotifications()) {
                defender.sendMessage("Wolf pummeled at you");
            }
        }
    }

    public void attackTarget(LivingEntity target) {
        double range = 5;
        Player player = getPlayer();

        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity.getType() != EntityType.WOLF) {
                continue;
            }

            Wolf wolf = (Wolf) entity;

            if (!wolf.isTamed() || (wolf.getOwner() != player) || wolf.isSitting()) {
                continue;
            }

            wolf.setTarget(target);
        }
    }

    /**
     * Handle the Call of the Wild ability.
     *
     * @param type The type of entity to summon.
     * @param summonAmount The amount of material needed to summon the entity
     */
    private void callOfTheWild(EntityType type, int summonAmount) {
        Player player = getPlayer();

        ItemStack heldItem = player.getItemInHand();
        int heldItemAmount = heldItem.getAmount();
        Location location = player.getLocation();

        if (heldItemAmount < summonAmount) {
            player.sendMessage(LocaleLoader.getString("Skills.NeedMore", StringUtils.getPrettyItemString(heldItem.getType())));
            return;
        }

        if (!rangeCheck(type)) {
            return;
        }

        int amount = Config.getInstance().getTamingCOTWAmount(type);
        int tamingCOTWLength = Config.getInstance().getTamingCOTWLength(type);

        for (int i = 0; i < amount; i++) {
            if (!summonAmountCheck(type)) {
                return;
            }

            location = Misc.getLocationOffset(location, 1);
            LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(location, type);

            FakeEntityTameEvent event = new FakeEntityTameEvent(entity, player);
            FactionsPrivateer.getInstance().getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                continue;
            }

            entity.setMetadata(mcMMO.entityMetadataKey, mcMMO.metadataValue);
            ((Tameable) entity).setOwner(player);
            entity.setRemoveWhenFarAway(false);

            addToTracker(entity);

            switch (type) {
                case OCELOT:
                    ((Ocelot) entity).setCatType(Ocelot.Type.values()[1 + Misc.getRandom().nextInt(3)]);
                    break;

                case WOLF:
                    entity.setMaxHealth(20.0);
                    entity.setHealth(entity.getMaxHealth());
                    break;

                case HORSE:
                    Horse horse = (Horse) entity;

                    entity.setMaxHealth(15.0 + (Misc.getRandom().nextDouble() * 15));
                    entity.setHealth(entity.getMaxHealth());
                    horse.setColor(Horse.Color.values()[Misc.getRandom().nextInt(Horse.Color.values().length)]);
                    horse.setStyle(Horse.Style.values()[Misc.getRandom().nextInt(Horse.Style.values().length)]);
                    horse.setJumpStrength(Math.max(AdvancedConfig.getInstance().getMinHorseJumpStrength(), Math.min(Math.min(Misc.getRandom().nextDouble(), Misc.getRandom().nextDouble()) * 2, AdvancedConfig.getInstance().getMaxHorseJumpStrength())));
                    //TODO: setSpeed, once available
                    break;

                default:
                    break;
            }

            if (Permissions.renamePets(player)) {
                entity.setCustomName(LocaleLoader.getString("Taming.Summon.Name.Format", player.getName(), StringUtils.getCustomNameEntityTypeString(type)));
            }

            ParticleEffectUtils.playCallOfTheWildEffect(entity);
        }

        player.setItemInHand(heldItemAmount == summonAmount ? null : new ItemStack(heldItem.getType(), heldItemAmount - summonAmount));

        String lifeSpan = "";
        if (tamingCOTWLength > 0) {
            lifeSpan = LocaleLoader.getString("Taming.Summon.Lifespan", tamingCOTWLength);
        }

        player.sendMessage(LocaleLoader.getString("Taming.Summon.Complete") + lifeSpan);
        player.playSound(location, Sound.FIREWORK_LARGE_BLAST2, 1F, 0.5F);
    }

    private boolean rangeCheck(EntityType type) {
        double range = Config.getInstance().getTamingCOTWRange();
        Player player = getPlayer();

        if (range == 0) {
            return true;
        }

        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity.getType() == type) {
                player.sendMessage(Taming.getCallOfTheWildFailureMessage(type));
                return false;
            }
        }

        return true;
    }

    private boolean summonAmountCheck(EntityType entityType) {
        Player player = getPlayer();

        int maxAmountSummons = Config.getInstance().getTamingCOTWMaxAmount(entityType);

        if (maxAmountSummons <= 0) {
            return true;
        }

        List<TrackedTamingEntity> trackedEntities = getTrackedEntities(entityType);
        int summonAmount = trackedEntities == null ? 0 : trackedEntities.size();

        if (summonAmount >= maxAmountSummons) {
            player.sendMessage(LocaleLoader.getString("Taming.Summon.Fail.TooMany", maxAmountSummons));
            return false;
        }

        return true;
    }

    protected static void addToTracker(LivingEntity livingEntity) {
        TrackedTamingEntity trackedEntity = new TrackedTamingEntity(livingEntity);

        if (!summonedEntities.containsKey(livingEntity.getType())) {
            summonedEntities.put(livingEntity.getType(), new ArrayList<TrackedTamingEntity>());
        }

        summonedEntities.get(livingEntity.getType()).add(trackedEntity);
    }

    protected static List<TrackedTamingEntity> getTrackedEntities(EntityType entityType) {
        return summonedEntities.get(entityType);
    }

    protected static void removeFromTracker(TrackedTamingEntity trackedEntity) {
        summonedEntities.get(trackedEntity.getLivingEntity().getType()).remove(trackedEntity);
    }
}
