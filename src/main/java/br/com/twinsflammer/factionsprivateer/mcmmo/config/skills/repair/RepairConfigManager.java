package br.com.twinsflammer.factionsprivateer.mcmmo.config.skills.repair;

import br.com.twinsflammer.factionsprivateer.mcmmo.skills.repair.repairables.Repairable;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RepairConfigManager {

    private final List<Repairable> repairables = new ArrayList<Repairable>();

    public RepairConfigManager(mcMMO plugin) {
        Pattern pattern = Pattern.compile("repair\\.(?:.+)\\.yml");
        File dataFolder = FactionsPrivateer.getInstance().getDataFolder();
        File vanilla = new File(dataFolder, "repair.vanilla.yml");

        if (!vanilla.exists()) {
            FactionsPrivateer.getInstance().saveResource("repair.vanilla.yml", false);
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
