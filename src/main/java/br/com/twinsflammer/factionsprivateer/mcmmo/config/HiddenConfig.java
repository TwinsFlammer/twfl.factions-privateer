package br.com.twinsflammer.factionsprivateer.mcmmo.config;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import org.bukkit.configuration.file.YamlConfiguration;

public class HiddenConfig {

    private static HiddenConfig instance;
    private String fileName;
    private YamlConfiguration config;
    private boolean chunkletsEnabled;
    private int conversionRate;
    private boolean useEnchantmentBuffs;
    private int uuidConvertAmount;
    private int mojangRateLimit;
    private long mojangLimitPeriod;

    public HiddenConfig(String fileName) {
        this.fileName = fileName;
        load();
    }

    public static HiddenConfig getInstance() {
        if (instance == null) {
            instance = new HiddenConfig("hidden.yml");
        }

        return instance;
    }

    public void load() {
        if (FactionsPrivateer.getInstance().getResource(fileName) != null) {
            config = YamlConfiguration.loadConfiguration(FactionsPrivateer.getInstance().getResource(fileName));
            chunkletsEnabled = config.getBoolean("Options.Chunklets", true);
            conversionRate = config.getInt("Options.ConversionRate", 1);
            useEnchantmentBuffs = config.getBoolean("Options.EnchantmentBuffs", true);
            uuidConvertAmount = config.getInt("Options.UUIDConvertAmount", 5);
            mojangRateLimit = config.getInt("Options.MojangRateLimit", 50000);
            mojangLimitPeriod = config.getLong("Options.MojangLimitPeriod", 600000);
        }
    }

    public boolean getChunkletsEnabled() {
        return chunkletsEnabled;
    }

    public int getConversionRate() {
        return conversionRate;
    }

    public boolean useEnchantmentBuffs() {
        return useEnchantmentBuffs;
    }

    public int getUUIDConvertAmount() {
        return uuidConvertAmount;
    }

    public int getMojangRateLimit() {
        return mojangRateLimit;
    }

    public long getMojangLimitPeriod() {
        return mojangLimitPeriod;
    }
}
