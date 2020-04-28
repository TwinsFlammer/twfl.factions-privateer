package com.redefocus.factionscaribe.mcmmo.skills.unarmed;

import com.redefocus.factionscaribe.mcmmo.config.AdvancedConfig;
import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.AbilityType;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SecondaryAbility;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.ToolType;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.mcmmo.skills.SkillManager;
import com.redefocus.factionscaribe.mcmmo.util.EventUtils;
import com.redefocus.factionscaribe.mcmmo.util.ItemUtils;
import com.redefocus.factionscaribe.mcmmo.util.Misc;
import com.redefocus.factionscaribe.mcmmo.util.Permissions;
import com.redefocus.factionscaribe.mcmmo.util.player.UserManager;
import com.redefocus.factionscaribe.mcmmo.util.skills.SkillUtils;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.SmoothBrick;

public class UnarmedManager extends SkillManager {

    public UnarmedManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, SkillType.UNARMED);
    }

    public boolean canActivateAbility() {
        return mcMMOPlayer.getToolPreparationMode(ToolType.FISTS) && Permissions.berserk(getPlayer());
    }

    public boolean canUseIronArm() {
        return Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.IRON_ARM);
    }

    public boolean canUseBerserk() {
        return mcMMOPlayer.getAbilityMode(AbilityType.BERSERK) && Permissions.berserk(getPlayer());
    }

    public boolean canDisarm(LivingEntity target) {
        return target instanceof Player && ((Player) target).getItemInHand().getType() != Material.AIR && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.DISARM);
    }

    public boolean canDeflect() {
        Player player = getPlayer();

        return ItemUtils.isUnarmed(player.getItemInHand()) && Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.DEFLECT);
    }

    public boolean canUseBlockCracker() {
        return Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.BLOCK_CRACKER);
    }

    public boolean blockCrackerCheck(BlockState blockState) {
        if (!SkillUtils.activationSuccessful(SecondaryAbility.BLOCK_CRACKER, getPlayer())) {
            return false;
        }

        MaterialData data = blockState.getData();

        switch (blockState.getType()) {
            case SMOOTH_BRICK:
                if (!Unarmed.blockCrackerSmoothBrick) {
                    return false;
                }

                // Yes, this is awkward, but it's the *right* way to do it.
                SmoothBrick smoothBrick = (SmoothBrick) data;

                if (smoothBrick.getMaterial() != Material.STONE) {
                    return false;
                }

                smoothBrick.setMaterial(Material.COBBLESTONE);
                return true;

            default:
                return false;
        }
    }

    /**
     * Check for disarm.
     *
     * @param defender The defending player
     */
    public void disarmCheck(Player defender) {
        if (SkillUtils.activationSuccessful(SecondaryAbility.DISARM, getPlayer(), getSkillLevel(), activationChance) && !hasIronGrip(defender)) {
            if (EventUtils.callDisarmEvent(defender).isCancelled()) {
                return;
            }

            Item item = Misc.dropItem(defender.getLocation(), defender.getItemInHand());

            if (item != null && AdvancedConfig.getInstance().getDisarmProtected()) {
                item.setMetadata(mcMMO.disarmedItemKey, UserManager.getPlayer(defender).getPlayerMetadata());
            }

            defender.setItemInHand(new ItemStack(Material.AIR));
            defender.sendMessage(LocaleLoader.getString("Skills.Disarmed"));
        }
    }

    /**
     * Check for arrow deflection.
     */
    public boolean deflectCheck() {
        if (SkillUtils.activationSuccessful(SecondaryAbility.DEFLECT, getPlayer(), getSkillLevel(), activationChance)) {
            getPlayer().sendMessage(LocaleLoader.getString("Combat.ArrowDeflect"));
            return true;
        }

        return false;
    }

    /**
     * Handle the effects of the Berserk ability
     *
     * @param damage The amount of damage initially dealt by the event
     */
    public double berserkDamage(double damage) {
        damage = (damage * Unarmed.berserkDamageModifier) - damage;

        return damage;
    }

    /**
     * Handle the effects of the Iron Arm ability
     */
    public double ironArm() {
        if (!SkillUtils.activationSuccessful(SecondaryAbility.IRON_ARM, getPlayer())) {
            return 0;
        }

        return Math.min(Unarmed.ironArmMinBonusDamage + (getSkillLevel() / Unarmed.ironArmIncreaseLevel), Unarmed.ironArmMaxBonusDamage);
    }

    /**
     * Check Iron Grip ability success
     *
     * @param defender The defending player
     * @return true if the defender was not disarmed, false otherwise
     */
    private boolean hasIronGrip(Player defender) {
        if (!Misc.isNPCEntity(defender) && Permissions.secondaryAbilityEnabled(defender, SecondaryAbility.IRON_GRIP) && SkillUtils.activationSuccessful(SecondaryAbility.IRON_GRIP, defender, skill)) {
            defender.sendMessage(LocaleLoader.getString("Unarmed.Ability.IronGrip.Defender"));
            getPlayer().sendMessage(LocaleLoader.getString("Unarmed.Ability.IronGrip.Attacker"));

            return true;
        }

        return false;
    }
}
