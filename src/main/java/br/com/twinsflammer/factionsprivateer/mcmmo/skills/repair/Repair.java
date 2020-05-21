package br.com.twinsflammer.factionsprivateer.mcmmo.skills.repair;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.AdvancedConfig;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import org.bukkit.Material;

public class Repair {

    public static int repairMasteryMaxBonusLevel = AdvancedConfig.getInstance().getRepairMasteryMaxLevel();
    public static double repairMasteryMaxBonus = AdvancedConfig.getInstance().getRepairMasteryMaxBonus();

    public static Material anvilMaterial = Config.getInstance().getRepairAnvilMaterial();
}
