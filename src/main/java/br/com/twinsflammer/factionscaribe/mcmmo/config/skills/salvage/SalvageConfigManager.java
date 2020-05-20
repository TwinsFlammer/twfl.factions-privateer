package br.com.twinsflammer.factionscaribe.mcmmo.config.skills.salvage;

import br.com.twinsflammer.factionscaribe.mcmmo.skills.salvage.salvageables.Salvageable;
import br.com.twinsflammer.factionscaribe.mcmmo.mcMMO;
import br.com.twinsflammer.factionscaribe.FactionsCaribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SalvageConfigManager {

    private final List<Salvageable> salvageables = new ArrayList<Salvageable>();

    public SalvageConfigManager(mcMMO plugin) {
        Pattern pattern = Pattern.compile("salvage\\.(?:.+)\\.yml");
        File dataFolder = FactionsCaribe.getInstance().getDataFolder();
        File vanilla = new File(dataFolder, "salvage.vanilla.yml");

        if (!vanilla.exists()) {
            FactionsCaribe.getInstance().saveResource("salvage.vanilla.yml", false);
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
