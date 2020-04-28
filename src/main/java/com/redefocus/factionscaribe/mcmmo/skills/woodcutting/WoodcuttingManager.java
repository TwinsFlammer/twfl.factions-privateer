package com.redefocus.factionscaribe.mcmmo.skills.woodcutting;

import com.redefocus.factionscaribe.mcmmo.datatypes.mods.CustomBlock;
import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.AbilityType;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SecondaryAbility;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.XPGainReason;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.mcmmo.skills.SkillManager;
import com.redefocus.factionscaribe.mcmmo.util.EventUtils;
import com.redefocus.factionscaribe.mcmmo.util.ItemUtils;
import com.redefocus.factionscaribe.mcmmo.util.Misc;
import com.redefocus.factionscaribe.mcmmo.util.Permissions;
import com.redefocus.factionscaribe.mcmmo.util.skills.CombatUtils;
import com.redefocus.factionscaribe.mcmmo.util.skills.SkillUtils;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Tree;

public class WoodcuttingManager extends SkillManager {

    public WoodcuttingManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, SkillType.WOODCUTTING);
    }

    public boolean canUseLeafBlower(ItemStack heldItem) {
        return Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.LEAF_BLOWER) && getSkillLevel() >= Woodcutting.leafBlowerUnlockLevel && ItemUtils.isAxe(heldItem);
    }

    public boolean canUseTreeFeller(ItemStack heldItem) {
        return mcMMOPlayer.getAbilityMode(AbilityType.TREE_FELLER) && Permissions.treeFeller(getPlayer()) && ItemUtils.isAxe(heldItem);
    }

    protected boolean canGetDoubleDrops() {
        return Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.WOODCUTTING_DOUBLE_DROPS) && SkillUtils.activationSuccessful(SecondaryAbility.WOODCUTTING_DOUBLE_DROPS, getPlayer(), getSkillLevel(), activationChance);
    }

    /**
     * Begins Woodcutting
     *
     * @param blockState Block being broken
     */
    public void woodcuttingBlockCheck(BlockState blockState) {
        int xp = Woodcutting.getExperienceFromLog(blockState, Woodcutting.ExperienceGainMethod.DEFAULT);

        switch (blockState.getType()) {
            case HUGE_MUSHROOM_1:
            case HUGE_MUSHROOM_2:
                break;

            default:
                if (canGetDoubleDrops()) {
                    Woodcutting.checkForDoubleDrop(blockState);
                }
        }

        applyXpGain(xp, XPGainReason.PVE);
    }

    /**
     * Begins Tree Feller
     *
     * @param blockState Block being broken
     */
    public void processTreeFeller(BlockState blockState) {
        Player player = getPlayer();
        Set<BlockState> treeFellerBlocks = new HashSet<BlockState>();

        Woodcutting.treeFellerReachedThreshold = false;

        Woodcutting.processTree(blockState, treeFellerBlocks);

        // If the player is trying to break too many blocks
        if (Woodcutting.treeFellerReachedThreshold) {
            Woodcutting.treeFellerReachedThreshold = false;

            player.sendMessage(LocaleLoader.getString("Woodcutting.Skills.TreeFeller.Threshold"));
            return;
        }

        // If the tool can't sustain the durability loss
        if (!Woodcutting.handleDurabilityLoss(treeFellerBlocks, player.getItemInHand())) {
            player.sendMessage(LocaleLoader.getString("Woodcutting.Skills.TreeFeller.Splinter"));

            double health = player.getHealth();

            if (health > 1) {
                CombatUtils.dealDamage(player, Misc.getRandom().nextInt((int) (health - 1)));
            }

            return;
        }

        dropBlocks(treeFellerBlocks);
        Woodcutting.treeFellerReachedThreshold = false; // Reset the value after we're done with Tree Feller each time.
    }

    /**
     * Handles the dropping of blocks
     *
     * @param treeFellerBlocks List of blocks to be dropped
     */
    private void dropBlocks(Set<BlockState> treeFellerBlocks) {
        Player player = getPlayer();
        int xp = 0;

        for (BlockState blockState : treeFellerBlocks) {
            Block block = blockState.getBlock();

            if (!EventUtils.simulateBlockBreak(block, player, true)) {
                break; // TODO: Shouldn't we use continue instead?
            }

            Material material = blockState.getType();

            if (material == Material.HUGE_MUSHROOM_1 || material == Material.HUGE_MUSHROOM_2) {
                xp += Woodcutting.getExperienceFromLog(blockState, Woodcutting.ExperienceGainMethod.TREE_FELLER);
                Misc.dropItems(blockState.getLocation(), block.getDrops());
            } else if (mcMMO.getModManager().isCustomLog(blockState)) {
                if (canGetDoubleDrops()) {
                    Woodcutting.checkForDoubleDrop(blockState);
                }

                CustomBlock customBlock = mcMMO.getModManager().getBlock(blockState);
                xp = customBlock.getXpGain();

                Misc.dropItems(blockState.getLocation(), block.getDrops());
            } else if (mcMMO.getModManager().isCustomLeaf(blockState)) {
                Misc.dropItems(blockState.getLocation(), block.getDrops());
            } else {
                //TODO Remove this workaround when casting to Tree works again
                if (blockState.getData() instanceof Tree) {
                    Tree tree = (Tree) blockState.getData();
                    tree.setDirection(BlockFace.UP);
                }

                switch (material) {
                    case LOG:
                    case LOG_2:
                        if (canGetDoubleDrops()) {
                            Woodcutting.checkForDoubleDrop(blockState);
                        }
                        xp += Woodcutting.getExperienceFromLog(blockState, Woodcutting.ExperienceGainMethod.TREE_FELLER);
                        Misc.dropItems(blockState.getLocation(), block.getDrops());
                        break;

                    case LEAVES:
                    case LEAVES_2:
                        Misc.dropItems(blockState.getLocation(), block.getDrops());
                        break;

                    default:
                        break;
                }
            }

            blockState.setType(Material.AIR);
            blockState.update(true);
        }

        applyXpGain(xp, XPGainReason.PVE);
    }
}
