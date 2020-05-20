package br.com.twinsflammer.factionscaribe.mcmmo.skills.excavation;

import br.com.twinsflammer.factionscaribe.mcmmo.config.Config;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SecondaryAbility;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.XPGainReason;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.treasure.ExcavationTreasure;
import br.com.twinsflammer.factionscaribe.mcmmo.skills.SkillManager;
import br.com.twinsflammer.factionscaribe.mcmmo.util.Misc;
import br.com.twinsflammer.factionscaribe.mcmmo.util.Permissions;
import br.com.twinsflammer.factionscaribe.mcmmo.util.skills.SkillUtils;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockState;

public class ExcavationManager extends SkillManager {

    public ExcavationManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, SkillType.EXCAVATION);
    }

    /**
     * Process treasure drops & XP gain for Excavation.
     *
     * @param blockState The {@link BlockState} to check ability activation for
     */
    public void excavationBlockCheck(BlockState blockState) {
        int xp = Excavation.getBlockXP(blockState);

        if (Permissions.secondaryAbilityEnabled(getPlayer(), SecondaryAbility.EXCAVATION_TREASURE_HUNTER)) {
            List<ExcavationTreasure> treasures = Excavation.getTreasures(blockState);

            if (!treasures.isEmpty()) {
                int skillLevel = getSkillLevel();
                Location location = blockState.getLocation();

                for (ExcavationTreasure treasure : treasures) {
                    if (skillLevel >= treasure.getDropLevel() && SkillUtils.treasureDropSuccessful(getPlayer(), treasure.getDropChance(), activationChance)) {
                        xp += treasure.getXp();
                        Misc.dropItem(location, treasure.getDrop());
                    }
                }
            }
        }

        applyXpGain(xp, XPGainReason.PVE);
    }

    /**
     * Process the Giga Drill Breaker ability.
     *
     * @param blockState The {@link BlockState} to check ability activation for
     */
    public void gigaDrillBreaker(BlockState blockState) {
        excavationBlockCheck(blockState);
        excavationBlockCheck(blockState);

        SkillUtils.handleDurabilityChange(getPlayer().getItemInHand(), Config.getInstance().getAbilityToolDamage());
    }
}
