package com.redefocus.factionscaribe.mcmmo.config.skills.repair;

import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.mcmmo.skills.repair.repairables.Repairable;
import com.redefocus.factionscaribe.FactionsCaribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RepairConfigManager {

    private final List<Repairable> repairables = new ArrayList<Repairable>();

    public RepairConfigManager(mcMMO plugin) {
        Pattern pattern = Pattern.compile("repair\\.(?:.+)\\.yml");
        File dataFolder = FactionsCaribe.getInstance().getDataFolder();
        File vanilla = new File(dataFolder, "repair.vanilla.yml");

        if (!vanilla.exists()) {
            FactionsCaribe.getInstance().saveResource("repair.vanilla.yml", false);
        }

        for (String fileName : dataFolder.list()) {
            if (!pattern.matcher(fileName).matches()) {
                continue;
            }

            File file = new File(dataFolder, fileName);

            if (file.isDirectory()) {
                continue;
            }

            RepairConfig rConfig = new RepairConfig(fileName);
            repairables.addAll(rConfig.getLoadedRepairables());
        }
    }

    public List<Repairable> getLoadedRepairables() {
        return repairables;
    }
}
