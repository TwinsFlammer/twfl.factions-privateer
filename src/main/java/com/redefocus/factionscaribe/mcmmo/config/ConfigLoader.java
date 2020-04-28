package com.redefocus.factionscaribe.mcmmo.config;

import com.redefocus.factionscaribe.mcmmo.mcMMO;
import java.io.File;
import java.util.List;

import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class ConfigLoader {

    protected static final mcMMO plugin = mcMMO.p;
    protected String fileName;
    private File configFile;
    protected FileConfiguration config;

    public ConfigLoader(String relativePath, String fileName) {
        this.fileName = fileName;
        configFile = new File(FactionsCaribe.getInstance().getDataFolder(), relativePath + File.separator + fileName);
        loadFile();
    }

    public ConfigLoader(String fileName) {
        this.fileName = fileName;
        configFile = new File(FactionsCaribe.getInstance().getDataFolder(), fileName);
        loadFile();
    }

    protected void loadFile() {
        if (!configFile.exists()) {
            plugin.debug("Creating mcMMO " + fileName + " File...");

            try {
                FactionsCaribe.getInstance().saveResource(fileName, false); // Normal files
            } catch (IllegalArgumentException ex) {
                FactionsCaribe.getInstance().saveResource(configFile.getParentFile().getName() + File.separator + fileName, false); // Mod files
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
            FactionsCaribe.getInstance().getLogger().warning(issue);
        }

        return issues.isEmpty();
    }

    protected void validate() {
        if (validateKeys()) {
            plugin.debug("No errors found in " + fileName + "!");
        } else {
            FactionsCaribe.getInstance().getLogger().warning("Errors were found in " + fileName + "! mcMMO was disabled!");
            FactionsCaribe.getInstance().getServer().getPluginManager().disablePlugin(FactionsCaribe.getInstance());
            plugin.noErrorsInConfigFiles = false;
        }
    }

    public File getFile() {
        return configFile;
    }

    public void backup() {
        FactionsCaribe.getInstance().getLogger().warning("You are using an old version of the " + fileName + " file.");
        FactionsCaribe.getInstance().getLogger().warning("Your old file has been renamed to " + fileName + ".old and has been replaced by an updated version.");

        configFile.renameTo(new File(configFile.getPath() + ".old"));

        if (FactionsCaribe.getInstance().getResource(fileName) != null) {
            FactionsCaribe.getInstance().saveResource(fileName, true);
        }

        FactionsCaribe.getInstance().getLogger().warning("Reloading " + fileName + " with new values...");
        loadFile();
        loadKeys();
    }
}
