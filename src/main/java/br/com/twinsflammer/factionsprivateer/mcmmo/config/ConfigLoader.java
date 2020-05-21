package br.com.twinsflammer.factionsprivateer.mcmmo.config;

import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import java.io.File;
import java.util.List;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class ConfigLoader {

    protected static final mcMMO plugin = mcMMO.p;
    protected String fileName;
    private File configFile;
    protected FileConfiguration config;

    public ConfigLoader(String relativePath, String fileName) {
        this.fileName = fileName;
        configFile = new File(FactionsPrivateer.getInstance().getDataFolder(), relativePath + File.separator + fileName);
        loadFile();
    }

    public ConfigLoader(String fileName) {
        this.fileName = fileName;
        configFile = new File(FactionsPrivateer.getInstance().getDataFolder(), fileName);
        loadFile();
    }

    protected void loadFile() {
        if (!configFile.exists()) {
            plugin.debug("Creating mcMMO " + fileName + " File...");

            try {
                FactionsPrivateer.getInstance().saveResource(fileName, false); // Normal files
            } catch (IllegalArgumentException ex) {
                FactionsPrivateer.getInstance().saveResource(configFile.getParentFile().getName() + File.separator + fileName, false); // Mod files
            }
        } else {
            plugin.debug("Loading mcMMO " + fileName + " File...");
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    protected abstract void loadKeys();

    protected boolean validateKeys() {
        return true;
    }

    protected boolean noErrorsInConfig(List<String> issues) {
        for (String issue : issues) {
            FactionsPrivateer.getInstance().getLogger().warning(issue);
        }

        return issues.isEmpty();
    }

    protected void validate() {
        if (validateKeys()) {
            plugin.debug("No errors found in " + fileName + "!");
        } else {
            FactionsPrivateer.getInstance().getLogger().warning("Errors were found in " + fileName + "! mcMMO was disabled!");
            FactionsPrivateer.getInstance().getServer().getPluginManager().disablePlugin(FactionsPrivateer.getInstance());
            plugin.noErrorsInConfigFiles = false;
        }
    }

    public File getFile() {
        return configFile;
    }

    public void backup() {
        FactionsPrivateer.getInstance().getLogger().warning("You are using an old version of the " + fileName + " file.");
        FactionsPrivateer.getInstance().getLogger().warning("Your old file has been renamed to " + fileName + ".old and has been replaced by an updated version.");

        configFile.renameTo(new File(configFile.getPath() + ".old"));

        if (FactionsPrivateer.getInstance().getResource(fileName) != null) {
            FactionsPrivateer.getInstance().saveResource(fileName, true);
        }

        FactionsPrivateer.getInstance().getLogger().warning("Reloading " + fileName + " with new values...");
        loadFile();
        loadKeys();
    }
}
