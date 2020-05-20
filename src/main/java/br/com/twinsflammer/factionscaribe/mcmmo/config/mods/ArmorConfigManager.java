package br.com.twinsflammer.factionscaribe.mcmmo.config.mods;

import br.com.twinsflammer.factionscaribe.mcmmo.mcMMO;
import br.com.twinsflammer.factionscaribe.mcmmo.util.ModManager;
import br.com.twinsflammer.factionscaribe.FactionsCaribe;

import java.io.File;
import java.util.regex.Pattern;

public class ArmorConfigManager {

    public ArmorConfigManager(mcMMO plugin) {
        Pattern middlePattern = Pattern.compile("armor\\.(?:.+)\\.yml");
        Pattern startPattern = Pattern.compile("(?:.+)\\.armor\\.yml");
        File dataFolder = new File(mcMMO.getModDirectory());
        File vanilla = new File(dataFolder, "armor.default.yml");
        ModManager modManager = mcMMO.getModManager();

        if (!vanilla.exists()) {
            FactionsCaribe.getInstance().saveResource(vanilla.getParentFile().getName() + File.separator + "armor.default.yml", false);
        }

        for (String fileName : dataFolder.list()) {
            if (!middlePattern.matcher(fileName).matches() && !startPattern.matcher(fileName).matches()) {
                continue;
            }

            File file = new File(dataFolder, fileName);

            if (file.isDirectory()) {
                continue;
            }

            modManager.registerCustomArmor(new CustomArmorConfig(fileName));
        }
    }
}
