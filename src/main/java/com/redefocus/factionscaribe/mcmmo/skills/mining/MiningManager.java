package com.redefocus.factionscaribe.mcmmo.skills.mining;

import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.AbilityType;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SecondaryAbility;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.XPGainReason;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.mcmmo.runnables.skills.AbilityCooldownTask;
import com.redefocus.factionscaribe.mcmmo.skills.SkillManager;
import com.redefocus.factionscaribe.mcmmo.skills.mining.BlastMining.Tier;
import com.redefocus.factionscaribe.mcmmo.util.BlockUtils;
import com.redefocus.factionscaribe.mcmmo.util.EventUtils;
import com.redefocus.factionscaribe.mcmmo.util.Misc;
import com.redefocus.factionscaribe.mcmmo.util.Permissions;
import com.redefocus.factionscaribe.mcmmo.util.skills.SkillUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

public class MiningManager extends SkillManager {

    public MiningManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, SkillType.MINING);
    }

    public boolean canUseDemolitionsExpertise() {
        return getSkillLevel() >= BlastMining.getDemolitionExpertUnlockLevel() && Permissions.demolitionsExpertise(getPlayer());
    }

    public boolean canDetonate() {
        Player player = getPlayer();

        return canUseBlastMining() && player.isSneaking() && player.getItemInHand().getType() == BlastMining.detonator && Permissions.remoteDetonation(player);
    }

    public boolean canUseBlastMining() {
        return getSkillLevel() >= BlastMining.Tier.ONE.getLevel();
    }

    public boolean canUseBiggerBombs() {
        return getSkillLevel() >= BlastMining.getBiggerBombsUnlockLevel() && Permissions.biggerBombs(getPlayer());
    }

    /**
     * Process double drops & XP gain for Mining.
     *
     * @param blockState The {@link BlockState} to check ability activation for
     */
    public void miningBlockCheck(BlockState blockState) {
        Player player = getPlayer();

        applyXpGain(Mining.getBlockXp(blockState), XPGainReason.PVE);

        if (!Permissions.secondaryAbilityEnabled(player, SecondaryAbility.MINING_DOUBLE_DROPS)) {
            return;
        }

        Material material = blockState.getType();

        if (mcMMOPlayer.getAbilityMode(skill.getAbility())) {
            SkillUtils.handleDurabilityChange(getPlayer().getItemInHand(), Config.getInstance().getAbilityToolDamage());
        }

        if ((mcMMO.getModManager().isCustomMiningBlock(blockState) && !mcMMO.getModManager().getBlock(blockState).isDoubleDropEnabled()) || material != Material.GLOWING_REDSTONE_ORE && !Config.getInstance().getDoubleDropsEnabled(skill, material)) {
            return;
        }

        boolean silkTouch = player.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH);

        for (int i = mcMMOPlayer.getAbilityMode(skill.getAbility()) ? 2 : 1; i != 0; i--) {
            if (SkillUtils.activationSuccessful(SecondaryAbility.MINING_DOUBLE_DROPS, getPlayer(), getSkillLevel(), activationChance)) {
                if (silkTouch) {
                    Mining.handleSilkTouchDrops(blockState);
                } else {
                    Mining.handleMiningDrops(blockState);
                }
            }
        }
    }

    /**
     * Detonate TNT for Blast Mining
     */
    public void remoteDetonation() {
        Player player = getPlayer();
        Block targetBlock = player.getTargetBlock((HashSet<Byte>) BlockUtils.getTransparentBlocks(), BlastMining.MAXIMUM_REMOTE_DETONATION_DISTANCE);

        if (targetBlock.getType() != Material.TNT || !EventUtils.simulateBlockBreak(targetBlock, player, true) || !blastMiningCooldownOver()) {
            return;
        }

        TNTPrimed tnt = player.getWorld().spawn(targetBlock.getLocation(), TNTPrimed.class);

        SkillUtils.sendSkillMessage(player, AbilityType.BLAST_MINING.getAbilityPlayer(player));
        player.sendMessage(LocaleLoader.getString("Mining.Blast.Boom"));

        tnt.setMetadata(mcMMO.tntMetadataKey, mcMMOPlayer.getPlayerMetadata());
        tnt.setFuseTicks(0);
        targetBlock.setType(Material.AIR);

        mcMMOPlayer.setAbilityDATS(AbilityType.BLAST_MINING, System.currentTimeMillis());
        mcMMOPlayer.setAbilityInformed(AbilityType.BLAST_MINING, false);
        new AbilityCooldownTask(mcMMOPlayer, AbilityType.BLAST_MINING).runTaskLaterAsynchronously(FactionsCaribe.getInstance(), AbilityType.BLAST_MINING.getCooldown() * Misc.TICK_CONVERSION_FACTOR);
    }

    /**
     * Handler for explosion drops and XP gain.
     *
     * @param yield The % of blocks to drop
     * @param blockList The list of blocks to drop
     */
    public void blastMiningDropProcessing(float yield, List<Block> blockList) {
        List<BlockState> ores = new ArrayList<BlockState>();
        List<BlockState> debris = new ArrayList<BlockState>();
        int xp = 0;

        float oreBonus = (float) (getOreBonus() / 100);
        float debrisReduction = (float) (getDebrisReduction() / 100);
        int dropMultiplier = getDropMultiplier();

        float debrisYield = yield - debrisReduction;

        for (Block block : blockList) {
            BlockState blockState = block.getState();

            if (BlockUtils.isOre(blockState)) {
                ores.add(blockState);
            } else {
                debris.add(blockState);
            }
        }

        for (BlockState blockState : ores) {
            if (Misc.getRandom().nextFloat() < (yield + oreBonus)) {
                if (!mcMMO.getPlaceStore().isTrue(blockState)) {
                    xp += Mining.getBlockXp(blockState);
                }

                Misc.dropItem(blockState.getLocation(), blockState.getData().toItemStack(1)); // Initial block that would have been dropped

                if (!mcMMO.getPlaceStore().isTrue(blockState)) {
                    for (int i = 1; i < dropMultiplier; i++) {
                        Mining.handleSilkTouchDrops(blockState); // Bonus drops - should drop the block & not the items
                    }
                }
            }
        }

        if (debrisYield > 0) {
            for (BlockState blockState : debris) {
                if (Misc.getRandom().nextFloat() < debrisYield) {
                    Misc.dropItems(blockState.getLocation(), blockState.getBlock().getDrops());
                }
            }
        }

        applyXpGain(xp, XPGainReason.PVE);
    }

    /**
     * Increases the blast radius of the explosion.
     *
     * @param radius to modify
     * @return modified radius
     */
    public float biggerBombs(float radius) {
        return (float) (radius + getBlastRadiusModifier());
    }

    public double processDemolitionsExpertise(double damage) {
        return damage * ((100.0D - getBlastDamageModifier()) / 100.0D);
    }

    /**
     * Gets the Blast Mining tier
     *
     * @return the Blast Mining tier
     */
    public int getBlastMiningTier() {
        int skillLevel = getSkillLevel();

        for (Tier tier : Tier.values()) {
            if (skillLevel >= tier.getLevel()) {
                return tier.toNumerical();
            }
        }

        return 0;
    }

    /**
     * Gets the Blast Mining tier
     *
     * @return the Blast Mining tier
     */
    public double getOreBonus() {
        int skillLevel = getSkillLevel();

        for (Tier tier : Tier.values()) {
            if (skillLevel >= tier.getLevel()) {
                return tier.getOreBonus();
            }
        }

        return 0;
    }

    /**
     * Gets the Blast Mining tier
     *
     * @return the Blast Mining tier
     */
    public double getDebrisReduction() {
        int skillLevel = getSkillLevel();

        for (Tier tier : Tier.values()) {
            if (skillLevel >= tier.getLevel()) {
                return tier.getDebrisReduction();
            }
        }

        return 0;
    }

    /**
     * Gets the Blast Mining tier
     *
     * @return the Blast Mining tier
     */
    public int getDropMultiplier() {
        int skillLevel = getSkillLevel();

        for (Tier tier : Tier.values()) {
            if (skillLevel >= tier.getLevel()) {
                return tier.getDropMultiplier();
            }
        }

        return 0;
    }

    /**
     * Gets the Blast Mining tier
     *
     * @return the Blast Mining tier
     */
    public double getBlastRadiusModifier() {
        int skillLevel = getSkillLevel();

        for (Tier tier : Tier.values()) {
            if (skillLevel >= tier.getLevel()) {
                return tier.getBlastRadiusModifier();
            }
        }

        return 0;
    }

    /**
     * Gets the Blast Mining tier
     *
     * @return the Blast Mining tier
     */
    public double getBlastDamageModifier() {
        int skillLevel = getSkillLevel();

        for (Tier tier : Tier.values()) {
            if (skillLevel >= tier.getLevel()) {
                return tier.getBlastDamageDecrease();
            }
        }

        return 0;
    }

    private boolean blastMiningCooldownOver() {
        int timeRemaining = mcMMOPlayer.calculateTimeRemaining(AbilityType.BLAST_MINING);

        if (timeRemaining > 0) {
            getPlayer().sendMessage(LocaleLoader.getString("Skills.TooTired", timeRemaining));
            return false;
        }

        return true;
    }
}
