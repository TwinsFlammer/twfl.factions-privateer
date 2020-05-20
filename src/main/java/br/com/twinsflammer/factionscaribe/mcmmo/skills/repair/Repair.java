package br.com.twinsflammer.factionscaribe.mcmmo.skills.repair;

import br.com.twinsflammer.factionscaribe.mcmmo.config.AdvancedConfig;
import br.com.twinsflammer.factionscaribe.mcmmo.config.Config;
import org.bukkit.Material;

public class Repair {

    public static int repairMasteryMaxBonusLevel = AdvancedConfig.getInstance().getRepairMasteryMaxLevel();
    public static double repairMasteryMaxBonus = AdvancedConfig.getInstance().getRepairMasteryMaxBonus();

    public static Material anvilMaterial = Config.getInstance().getRepairAnvilMaterial();
}
