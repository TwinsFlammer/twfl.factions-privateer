package br.com.twinsflammer.factionsprivateer.mcmmo.config.mods;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.ModManager;

import java.io.File;
import java.util.regex.Pattern;

public class BlockConfigManager {

    public BlockConfigManager(mcMMO plugin) {
        Pattern middlePattern = Pattern.compile("blocks\\.(?:.+)\\.yml");
        Pattern startPattern = Pattern.compile("(?:.+)\\.blocks\\.yml");
        File dataFolder = new File(mcMMO.getModDirectory());
        File vanilla = new File(dataFolder, "blocks.default.yml");
        ModManager modManager = mcMMO.getModManager();

        if (!vanilla.exists()) {
            FactionsPrivateer.getInstance().saveResource(vanilla.getParentFile().getName() + File.separator + "blocks.default.yml", false);
        }

        for (String fileName : dataFolder.list()) {
            if (!middlePattern.matcher(fileName).matches() && !startPattern.matcher(fileName).matches()) {
                continue;
            }

            File file = new File(dataFolder, fileName);

            if (file.isDirectory()) {
                continue;
            }

            modManager.registerCustomBlocks(new CustomBlockConfig(fileName));
        }
    }
}
