package com.redefocus.factionscaribe.mcmmo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.redefocus.api.spigot.commands.registry.CommandRegistry;
import com.redefocus.common.shared.Common;
import com.redefocus.factionscaribe.mcmmo.api.McMMoAPI;
import com.redefocus.factionscaribe.mcmmo.commands.SkillsCommand;
import com.redefocus.factionscaribe.mcmmo.config.AdvancedConfig;
import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.config.HiddenConfig;
import com.redefocus.factionscaribe.mcmmo.config.experience.ExperienceConfig;
import com.redefocus.factionscaribe.mcmmo.config.mods.ArmorConfigManager;
import com.redefocus.factionscaribe.mcmmo.config.mods.BlockConfigManager;
import com.redefocus.factionscaribe.mcmmo.config.mods.EntityConfigManager;
import com.redefocus.factionscaribe.mcmmo.config.mods.ToolConfigManager;
import com.redefocus.factionscaribe.mcmmo.config.skills.alchemy.PotionConfig;
import com.redefocus.factionscaribe.mcmmo.config.skills.repair.RepairConfigManager;
import com.redefocus.factionscaribe.mcmmo.config.skills.salvage.SalvageConfigManager;
import com.redefocus.factionscaribe.mcmmo.config.treasure.TreasureConfig;
import com.redefocus.factionscaribe.mcmmo.database.DatabaseManager;
import com.redefocus.factionscaribe.mcmmo.database.DatabaseManagerFactory;
import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.datatypes.player.PlayerProfile;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.listeners.BlockListener;
import com.redefocus.factionscaribe.mcmmo.listeners.EntityListener;
import com.redefocus.factionscaribe.mcmmo.listeners.InventoryListener;
import com.redefocus.factionscaribe.mcmmo.listeners.PlayerListener;
import com.redefocus.factionscaribe.mcmmo.listeners.SelfListener;
import com.redefocus.factionscaribe.mcmmo.listeners.WorldListener;
import com.redefocus.factionscaribe.mcmmo.party.PartyManager;
import com.redefocus.factionscaribe.mcmmo.runnables.SaveTimerTask;
import com.redefocus.factionscaribe.mcmmo.runnables.UpdaterResultAsyncTask;
import com.redefocus.factionscaribe.mcmmo.runnables.backups.CleanBackupsTask;
import com.redefocus.factionscaribe.mcmmo.runnables.database.UserPurgeTask;
import com.redefocus.factionscaribe.mcmmo.runnables.party.PartyAutoKickTask;
import com.redefocus.factionscaribe.mcmmo.runnables.player.ClearRegisteredXPGainTask;
import com.redefocus.factionscaribe.mcmmo.runnables.player.PlayerProfileLoadingTask;
import com.redefocus.factionscaribe.mcmmo.runnables.player.PowerLevelUpdatingTask;
import com.redefocus.factionscaribe.mcmmo.runnables.skills.BleedTimerTask;
import com.redefocus.factionscaribe.mcmmo.skills.alchemy.Alchemy;
import com.redefocus.factionscaribe.mcmmo.skills.child.ChildConfig;
import com.redefocus.factionscaribe.mcmmo.skills.repair.repairables.Repairable;
import com.redefocus.factionscaribe.mcmmo.skills.repair.repairables.RepairableManager;
import com.redefocus.factionscaribe.mcmmo.skills.repair.repairables.SimpleRepairableManager;
import com.redefocus.factionscaribe.mcmmo.skills.salvage.salvageables.Salvageable;
import com.redefocus.factionscaribe.mcmmo.skills.salvage.salvageables.SalvageableManager;
import com.redefocus.factionscaribe.mcmmo.skills.salvage.salvageables.SimpleSalvageableManager;
import com.redefocus.factionscaribe.mcmmo.tasks.McMMOMusicTextLevel;
import com.redefocus.factionscaribe.mcmmo.tasks.McMMORanksTask;
import com.redefocus.factionscaribe.mcmmo.util.HolidayManager;
import com.redefocus.factionscaribe.mcmmo.util.LogFilter;
import com.redefocus.factionscaribe.mcmmo.util.Misc;
import com.redefocus.factionscaribe.mcmmo.util.ModManager;
import com.redefocus.factionscaribe.mcmmo.util.Permissions;
import com.redefocus.factionscaribe.mcmmo.util.blockmeta.chunkmeta.ChunkManager;
import com.redefocus.factionscaribe.mcmmo.util.blockmeta.chunkmeta.ChunkManagerFactory;
import com.redefocus.factionscaribe.mcmmo.util.commands.CommandRegistrationManager;
import com.redefocus.factionscaribe.mcmmo.util.experience.FormulaManager;
import com.redefocus.factionscaribe.mcmmo.util.player.UserManager;
import com.redefocus.factionscaribe.mcmmo.util.scoreboards.ScoreboardManager;
import com.redefocus.factionscaribe.mcmmo.util.upgrade.UpgradeManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import lombok.Getter;
import net.shatteredlands.shatt.backup.ZipLibrary;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;

public class mcMMO {

    public static Boolean musicText = true;

    /* Managers */
    private static ChunkManager placeStore;
    private static RepairableManager repairableManager;
    private static SalvageableManager salvageableManager;
    private static ModManager modManager;
    private static DatabaseManager databaseManager;
    private static FormulaManager formulaManager;
    private static HolidayManager holidayManager;
    private static UpgradeManager upgradeManager;

    /* File Paths */
    private static String mainDirectory;
    private static String flatFileDirectory;
    private static String usersFile;
    private static String modDirectory;

    public static mcMMO p;

    // Update Check
    private boolean updateAvailable;

    /* Plugin Checks */
    private static boolean healthBarPluginEnabled;

    // Config Validation Check
    public boolean noErrorsInConfigFiles = true;

    // XP Event Check
    private boolean xpEventEnabled;

    /* Metadata Values */
    public final static String entityMetadataKey = "mcMMO: Spawned Entity";
    public final static String blockMetadataKey = "mcMMO: Piston Tracking";
    public final static String furnaceMetadataKey = "mcMMO: Tracked Furnace";
    public final static String tntMetadataKey = "mcMMO: Tracked TNT";
    public final static String tntsafeMetadataKey = "mcMMO: Safe TNT";
    public final static String customNameKey = "mcMMO: Custom Name";
    public final static String customVisibleKey = "mcMMO: Name Visibility";
    public final static String droppedItemKey = "mcMMO: Tracked Item";
    public final static String infiniteArrowKey = "mcMMO: Infinite Arrow";
    public final static String bowForceKey = "mcMMO: Bow Force";
    public final static String arrowDistanceKey = "mcMMO: Arrow Distance";
    public final static String customDamageKey = "mcMMO: Custom Damage";
    public final static String disarmedItemKey = "mcMMO: Disarmed Item";
    public final static String playerDataKey = "mcMMO: Player Data";
    public final static String greenThumbDataKey = "mcMMO: Green Thumb";
    public final static String databaseCommandKey = "mcMMO: Processing Database Command";
    public final static String bredMetadataKey = "mcMMO: Bred Animal";

    public static FixedMetadataValue metadataValue;

    public static boolean VIPS_KEEP_ENCHANTS = false;
    public static boolean REPAIR_SHIFT_ONLY = false;

    @Getter
    private static List<PlayerProfile> playerProfiles;

    @Getter
    private static HashMap<SkillType, PlayerProfile> topSkillsPlayerProfile;

    /**
     * Things to be run when the plugin is enabled.
     */
    public void onEnable() {
        try {
            p = this;
            FactionsCaribe.getInstance().getLogger().setFilter(new LogFilter(FactionsCaribe.getInstance().getConfig().getBoolean("General.Verbose_Logging")));
            metadataValue = new FixedMetadataValue(FactionsCaribe.getInstance(), true);

            PluginManager pluginManager = FactionsCaribe.getInstance().getServer().getPluginManager();
            healthBarPluginEnabled = pluginManager.getPlugin("HealthBar") != null;

            upgradeManager = new UpgradeManager();

            setupFilePaths();

            modManager = new ModManager();

            loadConfigFiles();

            if (!noErrorsInConfigFiles) {
                return;
            }

            if (Bukkit.getServer().getName().equals("Cauldron") || Bukkit.getServer().getName().equals("MCPC+")) {
                checkModConfigs();
            }

            if (healthBarPluginEnabled) {
                Bukkit.getLogger().info("HealthBar plugin found, mcMMO's healthbars are automatically disabled.");
            }

            if (pluginManager.getPlugin("NoCheatPlus") != null && pluginManager.getPlugin("CompatNoCheatPlus") == null) {
                Bukkit.getLogger().warning("NoCheatPlus plugin found, but CompatNoCheatPlus was not found!");
                Bukkit.getLogger().warning("mcMMO will not work properly alongside NoCheatPlus without CompatNoCheatPlus");
            }

            databaseManager = DatabaseManagerFactory.getDatabaseManager();

            registerEvents();
            registerCustomRecipes();

            PartyManager.loadParties();

            formulaManager = new FormulaManager();
            holidayManager = new HolidayManager();

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                new PlayerProfileLoadingTask(player).runTaskLaterAsynchronously(FactionsCaribe.getInstance(), 0); // 1 Tick delay to ensure the player is marked as online before we begin loading

            }
            Bukkit.getScheduler().runTaskTimerAsynchronously(FactionsCaribe.getInstance(), new McMMORanksTask(), 20L, 20L * 60);
            Bukkit.getScheduler().runTaskTimerAsynchronously(FactionsCaribe.getInstance(), new McMMOMusicTextLevel(), 0, 5);

            //PreferenceConnector.addPreferenceBridge(new PreferenceBridge(PreferenceSeeTextLevel.INSTANCE, new ItemStack(Material.EXP_BOTTLE)));
            //PreferenceManager.registerPreference(PreferenceSeeTextLevel.INSTANCE);
            //debug("Version " + getDescription().getVersion() + " is enabled!");
            scheduleTasks();
            CommandRegistrationManager.registerCommands();
            //CommandRegistry.registerCommand(new CommandMusicText());

            placeStore = ChunkManagerFactory.getChunkManager(); // Get our ChunkletManager

            checkForUpdates();

            //CustomMining.mcMMODrops = new CustomMiningAPI();
            if (Config.getInstance().getPTPCommandWorldPermissions()) {
                Permissions.generateWorldTeleportPermissions();
            }

            CommandRegistry.registerCommand(
                    FactionsCaribe.getInstance(),
                    new SkillsCommand()
            );

            mcMMO.playerProfiles = Lists.newArrayList();
            mcMMO.topSkillsPlayerProfile = Maps.newHashMap();

            Common.getInstance().getScheduler().scheduleAtFixedRate(
                    () -> {
                        for (int i = 0; i < 10; i++) {
                            String name = McMMoAPI.getTopAllName(i);

                            UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());

                            PlayerProfile playerProfile = mcMMO.getDatabaseManager().loadPlayerProfile(name, uuid, true);

                            if (playerProfile.isLoaded()) {
                                mcMMO.playerProfiles.add(i, playerProfile);
                            }
                        }

                        CaribeUser.DisplaySkill[] displaySkills = CaribeUser.DisplaySkill.values();

                        for (int i = 0; i < displaySkills.length; i++) {
                            SkillType skillType = SkillType.valueOf(displaySkills[i].getSkillName());

                            String name = McMMoAPI.getTopSkillName(skillType, i);

                            UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());

                            PlayerProfile playerProfile = mcMMO.getDatabaseManager().loadPlayerProfile(name, uuid, true);

                            if (playerProfile.isLoaded()) {
                                mcMMO.topSkillsPlayerProfile.put(skillType, playerProfile);
                            }
                        }
                    },
                    0,
                    5,
                    TimeUnit.MINUTES
            );
        } catch (SecurityException | IllegalArgumentException | IllegalStateException t) {
            FactionsCaribe.getInstance().getLogger().severe("There was an error while enabling mcMMO!");
            t.printStackTrace();
            FactionsCaribe.getInstance().getServer().getPluginManager().disablePlugin(FactionsCaribe.getInstance());
        }
    }

    /**
     * Things to be run when the plugin is disabled.
     */
    public void onDisable() {
        try {
            Alchemy.finishAllBrews();   // Finish all partially complete AlchemyBrewTasks to prevent vanilla brewing continuation on restart
            UserManager.saveAll();      // Make sure to save player information if the server shuts down
            UserManager.clearAll();
            PartyManager.saveParties(); // Save our parties
            ScoreboardManager.teardownAll();
            formulaManager.saveFormula();
            holidayManager.saveAnniversaryFiles();
            placeStore.saveAll();       // Save our metadata
            placeStore.cleanUp();       // Cleanup empty metadata stores
        } catch (NullPointerException e) {
        }

        debug("Canceling all tasks...");
        Bukkit.getServer().getScheduler().cancelTasks(FactionsCaribe.getInstance()); // This removes our tasks
        debug("Unregister all events...");

        if (Config.getInstance().getBackupsEnabled()) {
            // Remove other tasks BEFORE starting the Backup, or we just cancel it straight away.
            try {
                ZipLibrary.mcMMOBackup();
            } catch (IOException e) {
                FactionsCaribe.getInstance().getServer().getLogger().severe(e.toString());
            } catch (Throwable e) {
                if (e instanceof NoClassDefFoundError) {
                    FactionsCaribe.getInstance().getServer().getLogger().severe("Backup class not found!");
                    FactionsCaribe.getInstance().getServer().getLogger().info("Please do not replace the mcMMO jar while the server is running.");
                } else {
                    FactionsCaribe.getInstance().getServer().getLogger().severe(e.toString());
                }
            }
        }

        databaseManager.onDisable();
        debug("Was disabled."); // How informative!
    }

    public static String getMainDirectory() {
        return mainDirectory;
    }

    public static String getFlatFileDirectory() {
        return flatFileDirectory;
    }

    public static String getUsersFilePath() {
        return usersFile;
    }

    public static String getModDirectory() {
        return modDirectory;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public void setUpdateAvailable(boolean available) {
        this.updateAvailable = available;
    }

    public boolean isXPEventEnabled() {
        return xpEventEnabled;
    }

    public void setXPEventEnabled(boolean enabled) {
        this.xpEventEnabled = enabled;
    }

    public void toggleXpEventEnabled() {
        xpEventEnabled = !xpEventEnabled;
    }

    public void debug(String message) {
        FactionsCaribe.getInstance().getServer().getLogger().info("[Debug] " + message);
    }

    public static FormulaManager getFormulaManager() {
        return formulaManager;
    }

    public static HolidayManager getHolidayManager() {
        return holidayManager;
    }

    public static ChunkManager getPlaceStore() {
        return placeStore;
    }

    public static RepairableManager getRepairableManager() {
        return repairableManager;
    }

    public static SalvageableManager getSalvageableManager() {
        return salvageableManager;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static ModManager getModManager() {
        return modManager;
    }

    public static UpgradeManager getUpgradeManager() {
        return upgradeManager;
    }

    @Deprecated
    public static void setDatabaseManager(DatabaseManager databaseManager) {
        mcMMO.databaseManager = databaseManager;
    }

    public static boolean isHealthBarPluginEnabled() {
        return healthBarPluginEnabled;
    }

    /**
     * Setup the various storage file paths
     */
    private void setupFilePaths() {
        mainDirectory = FactionsCaribe.getInstance().getDataFolder().getPath() + File.separator;
        flatFileDirectory = mainDirectory + "flatfile" + File.separator;
        usersFile = flatFileDirectory + "mcmmo.users";
        modDirectory = mainDirectory + "mods" + File.separator;
        fixFilePaths();
    }

    private void fixFilePaths() {
        File oldFlatfilePath = new File(mainDirectory + "FlatFileStuff" + File.separator);
        File oldModPath = new File(mainDirectory + "ModConfigs" + File.separator);

        if (oldFlatfilePath.exists()) {
            if (!oldFlatfilePath.renameTo(new File(flatFileDirectory))) {
                FactionsCaribe.getInstance().getLogger().warning("Failed to rename FlatFileStuff to flatfile!");
            }
        }

        if (oldModPath.exists()) {
            if (!oldModPath.renameTo(new File(modDirectory))) {
                FactionsCaribe.getInstance().getLogger().warning("Failed to rename ModConfigs to mods!");
            }
        }

        File oldArmorFile = new File(modDirectory + "armor.yml");
        File oldBlocksFile = new File(modDirectory + "blocks.yml");
        File oldEntitiesFile = new File(modDirectory + "entities.yml");
        File oldToolsFile = new File(modDirectory + "tools.yml");

        if (oldArmorFile.exists()) {
            if (!oldArmorFile.renameTo(new File(modDirectory + "armor.default.yml"))) {
                FactionsCaribe.getInstance().getServer().getLogger().warning("Failed to rename armor.yml to armor.default.yml!");
            }
        }

        if (oldBlocksFile.exists()) {
            if (!oldBlocksFile.renameTo(new File(modDirectory + "blocks.default.yml"))) {
                FactionsCaribe.getInstance().getServer().getLogger().warning("Failed to rename blocks.yml to blocks.default.yml!");
            }
        }

        if (oldEntitiesFile.exists()) {
            if (!oldEntitiesFile.renameTo(new File(modDirectory + "entities.default.yml"))) {
                FactionsCaribe.getInstance().getServer().getLogger().warning("Failed to rename entities.yml to entities.default.yml!");
            }
        }

        if (oldToolsFile.exists()) {
            if (!oldToolsFile.renameTo(new File(modDirectory + "tools.default.yml"))) {
                FactionsCaribe.getInstance().getServer().getLogger().warning("Failed to rename tools.yml to tools.default.yml!");
            }
        }

        File currentFlatfilePath = new File(flatFileDirectory);
        currentFlatfilePath.mkdirs();
    }

    private void checkForUpdates() {
        if (!Config.getInstance().getUpdateCheckEnabled()) {
            return;
        }

        new UpdaterResultAsyncTask(this).runTaskAsynchronously(FactionsCaribe.getInstance());
    }

    private void loadConfigFiles() {
        // Force the loading of config files
        TreasureConfig.getInstance();
        HiddenConfig.getInstance();
        AdvancedConfig.getInstance();
        PotionConfig.getInstance();
        new ChildConfig();

        List<Repairable> repairables = new ArrayList<Repairable>();
        List<Salvageable> salvageables = new ArrayList<Salvageable>();

        if (Config.getInstance().getToolModsEnabled()) {
            new ToolConfigManager(this);
        }

        if (Config.getInstance().getArmorModsEnabled()) {
            new ArmorConfigManager(this);
        }

        if (Config.getInstance().getBlockModsEnabled()) {
            new BlockConfigManager(this);
        }

        if (Config.getInstance().getEntityModsEnabled()) {
            new EntityConfigManager(this);
        }

        // Load repair configs, make manager, and register them at this time
        repairables.addAll(new RepairConfigManager(this).getLoadedRepairables());
        repairables.addAll(modManager.getLoadedRepairables());
        repairableManager = new SimpleRepairableManager(repairables.size());
        repairableManager.registerRepairables(repairables);

        // Load salvage configs, make manager and register them at this time
        SalvageConfigManager sManager = new SalvageConfigManager(this);
        salvageables.addAll(sManager.getLoadedSalvageables());
        salvageableManager = new SimpleSalvageableManager(salvageables.size());
        salvageableManager.registerSalvageables(salvageables);
    }

    private void registerEvents() {
        PluginManager pluginManager = FactionsCaribe.getInstance().getServer().getPluginManager();

        // Register events
        pluginManager.registerEvents(new PlayerListener(this), FactionsCaribe.getInstance());
        pluginManager.registerEvents(new BlockListener(this), FactionsCaribe.getInstance());
        pluginManager.registerEvents(new EntityListener(this), FactionsCaribe.getInstance());
        pluginManager.registerEvents(new InventoryListener(this), FactionsCaribe.getInstance());
        pluginManager.registerEvents(new SelfListener(), FactionsCaribe.getInstance());
        pluginManager.registerEvents(new WorldListener(this), FactionsCaribe.getInstance());
    }

    private void registerCustomRecipes() {
//        if (Config.getInstance().getChimaeraEnabled()) {
//            getServer().addRecipe(ChimaeraWing.getChimaeraWingRecipe());
//        }

//        if (Config.getInstance().getFluxPickaxeEnabled()) {
//            getServer().addRecipe(SmeltingManager.getFluxPickaxeRecipe(Material.DIAMOND_PICKAXE));
//            getServer().addRecipe(SmeltingManager.getFluxPickaxeRecipe(Material.GOLD_PICKAXE));
//            getServer().addRecipe(SmeltingManager.getFluxPickaxeRecipe(Material.IRON_PICKAXE));
//            getServer().addRecipe(SmeltingManager.getFluxPickaxeRecipe(Material.STONE_PICKAXE));
//            getServer().addRecipe(SmeltingManager.getFluxPickaxeRecipe(Material.WOOD_PICKAXE));
//        }
    }

    private void scheduleTasks() {
        // Periodic save timer (Saves every 10 minutes by default)
        long saveIntervalTicks = Config.getInstance().getSaveInterval() * 1200;
        new SaveTimerTask().runTaskTimerAsynchronously(FactionsCaribe.getInstance(), saveIntervalTicks, saveIntervalTicks);

        // Cleanup the backups folder
        new CleanBackupsTask().runTaskAsynchronously(FactionsCaribe.getInstance());

        // Bleed timer (Runs every two seconds)      
        new BleedTimerTask().runTaskTimerAsynchronously(FactionsCaribe.getInstance(), 2 * Misc.TICK_CONVERSION_FACTOR, 2 * Misc.TICK_CONVERSION_FACTOR);

        // Old & Powerless User remover
        long purgeIntervalTicks = Config.getInstance().getPurgeInterval() * 60L * 60L * Misc.TICK_CONVERSION_FACTOR;

        if (purgeIntervalTicks == 0) {
            new UserPurgeTask().runTaskLaterAsynchronously(FactionsCaribe.getInstance(), 2 * Misc.TICK_CONVERSION_FACTOR); // Start 2 seconds after startup.
        } else if (purgeIntervalTicks > 0) {
            new UserPurgeTask().runTaskTimerAsynchronously(FactionsCaribe.getInstance(), purgeIntervalTicks, purgeIntervalTicks);
        }

        // Automatically remove old members from parties
        long kickIntervalTicks = Config.getInstance().getAutoPartyKickInterval() * 60L * 60L * Misc.TICK_CONVERSION_FACTOR;

        if (kickIntervalTicks == 0) {
            new PartyAutoKickTask().runTaskLater(FactionsCaribe.getInstance(), 2 * Misc.TICK_CONVERSION_FACTOR); // Start 2 seconds after startup.
        } else if (kickIntervalTicks > 0) {
            new PartyAutoKickTask().runTaskTimer(FactionsCaribe.getInstance(), kickIntervalTicks, kickIntervalTicks);
        }

        // Update power level tag scoreboards
        new PowerLevelUpdatingTask().runTaskTimer(FactionsCaribe.getInstance(), 2 * Misc.TICK_CONVERSION_FACTOR, 2 * Misc.TICK_CONVERSION_FACTOR);

//        if (getHolidayManager().nearingAprilFirst()) {
//            new CheckDateTask().runTaskTimer(this, 10L * Misc.TICK_CONVERSION_FACTOR, 1L * 60L * 60L * Misc.TICK_CONVERSION_FACTOR);
//        }
        // Clear the registered XP data so players can earn XP again
        if (ExperienceConfig.getInstance().getDiminishedReturnsEnabled()) {
            new ClearRegisteredXPGainTask().runTaskTimer(FactionsCaribe.getInstance(), 60, 60);
        }
    }

    private void checkModConfigs() {
        if (!Config.getInstance().getToolModsEnabled()) {
            FactionsCaribe.getInstance().getServer().getLogger().warning("Cauldron implementation found, but the custom tool config for mcMMO is disabled!");
            FactionsCaribe.getInstance().getServer().getLogger().info("To enable, set Mods.Tool_Mods_Enabled to TRUE in config.yml.");
        }

        if (!Config.getInstance().getArmorModsEnabled()) {
            FactionsCaribe.getInstance().getServer().getLogger().warning("Cauldron implementation found, but the custom armor config for mcMMO is disabled!");
            FactionsCaribe.getInstance().getServer().getLogger().info("To enable, set Mods.Armor_Mods_Enabled to TRUE in config.yml.");
        }

        if (!Config.getInstance().getBlockModsEnabled()) {
            FactionsCaribe.getInstance().getServer().getLogger().warning("Cauldron implementation found, but the custom block config for mcMMO is disabled!");
            FactionsCaribe.getInstance().getServer().getLogger().info("To enable, set Mods.Block_Mods_Enabled to TRUE in config.yml.");
        }

        if (!Config.getInstance().getEntityModsEnabled()) {
            FactionsCaribe.getInstance().getServer().getLogger().warning("Cauldron implementation found, but the custom entity config for mcMMO is disabled!");
            FactionsCaribe.getInstance().getServer().getLogger().info("To enable, set Mods.Entity_Mods_Enabled to TRUE in config.yml.");
        }
    }
}
