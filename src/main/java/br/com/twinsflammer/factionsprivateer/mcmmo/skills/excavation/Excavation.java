package br.com.twinsflammer.factionsprivateer.mcmmo.skills.excavation;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.experience.ExperienceConfig;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.treasure.TreasureConfig;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.treasure.ExcavationTreasure;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.List;

public class Excavation {

    /**
     * Get the list of possible {@link ExcavationTreasure |ExcavationTreasures}
     * obtained from a given block.
     *
     * @param blockState The {@link BlockState} of the block to check.
     * @return the list of treasures that could be found
     */
    protected static List<ExcavationTreasure> getTreasures(BlockState blockState) {
        switch (blockState.getType()) {
            case DIRT:
                return blockState.getRawData() == 0x2 ? TreasureConfig.getInstance().excavationFromPodzol : TreasureConfig.getInstance().excavationFromDirt;

            case GRASS:
                return TreasureConfig.getInstance().excavationFromGrass;

            case SAND:
                return blockState.getRawData() == 0x1 ? TreasureConfig.getInstance().excavationFromRedSand : TreasureConfig.getInstance().excavationFromSand;

            case GRAVEL:
                return TreasureConfig.getInstance().excavationFromGravel;

            case CLAY:
                return TreasureConfig.getInstance().excavationFromClay;

            case MYCEL:
                return TreasureConfig.getInstance().excavationFromMycel;

            case SOUL_SAND:
                return TreasureConfig.getInstance().excavationFromSoulSand;

            case SNOW:
                return TreasureConfig.getInstance().excavationFromSnow;

            default:
                return new ArrayList<ExcavationTreasure>();
        }
    }

    protected static int getBlockXP(BlockState blockState) {
        Material material = blockState.getType();
        int xp;

        if (material == Material.DIRT || material == Material.SAND) {
            xp = ExperienceConfig.getInstance().getDirtAndSandXp(blockState.getData());
        } else {
            xp = ExperienceConfig.getInstance().getXp(SkillType.EXCAVATION, material);
        }

        if (xp == 0 && mcMMO.getModManager().isCustomExcavationBlock(blockState)) {
            xp = mcMMO.getModManager().getBlock(blockState).getXpGain();
        }

        return xp;
    }
}
