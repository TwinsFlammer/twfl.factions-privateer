package br.com.twinsflammer.factionsprivateer.mcmmo.config.skills.salvage;

import br.com.twinsflammer.factionsprivateer.mcmmo.skills.salvage.salvageables.Salvageable;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SalvageConfigManager {

    private final List<Salvageable> salvageables = new ArrayList<Salvageable>();

    public SalvageConfigManager(mcMMO plugin) {
        Pattern pattern = Pattern.compile("salvage\\.(?:.+)\\.yml");
        File dataFolder = FactionsPrivateer.getInstance().getDataFolder();
        File vanilla = new File(dataFolder, "salvage.vanilla.yml");

        if (!vanilla.exists()) {
            FactionsPrivateer.getInstance().saveResource("salvage.vanilla.yml", false);
        }

        for (String fileName : dataFolder.list()) {
            if (!pattern.matcher(fileName).matches()) {
                continue;
            }

            File file = new File(dataFolder, fileName);

            if (file.isDirectory()) {
                continue;
            }

            SalvageConfig salvageConfig = new SalvageConfig(fileName);
            salvageables.addAll(salvageConfig.getLoadedSalvageables());
        }
    }

    public List<Salvageable> getLoadedSalvageables() {
        return salvageables;
    }
}
