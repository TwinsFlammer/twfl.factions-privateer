package com.redefocus.factionscaribe.mcmmo.runnables.database;

import com.redefocus.factionscaribe.mcmmo.config.HiddenConfig;
import com.redefocus.factionscaribe.mcmmo.database.DatabaseManager;
import com.redefocus.factionscaribe.mcmmo.datatypes.database.UpgradeType;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.mcmmo.util.Misc;
import com.redefocus.factionscaribe.mcmmo.util.uuid.UUIDFetcher;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.scheduler.BukkitRunnable;

public class UUIDUpdateAsyncTask extends BukkitRunnable {

    private mcMMO plugin;
    private static final int MAX_LOOKUP = Math.max(HiddenConfig.getInstance().getUUIDConvertAmount(), 100);
    private static final int RATE_LIMIT = HiddenConfig.getInstance().getMojangRateLimit();
    private static final long LIMIT_PERIOD = HiddenConfig.getInstance().getMojangLimitPeriod();
    private static final int BATCH_SIZE = MAX_LOOKUP * 3;

    private List<String> userNames;
    private int size;
    private int checkedUsers;
    private long startMillis;

    public UUIDUpdateAsyncTask(mcMMO plugin, List<String> userNames) {
        this.plugin = plugin;
        this.userNames = userNames;

        this.checkedUsers = 0;
        this.startMillis = System.currentTimeMillis();
    }

    @Override
    public void run() {
        size = userNames.size();

        FactionsCaribe.getInstance().getLogger().info("Starting to check and update UUIDs, total amount of users: " + size);

        List<String> userNamesSection;
        Map<String, UUID> fetchedUUIDs = new HashMap<String, UUID>();

        while (size != 0) {
            if (checkedUsers + 100 > RATE_LIMIT) {
                try {
                    Thread.sleep(LIMIT_PERIOD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                startMillis = System.currentTimeMillis();
                checkedUsers = 0;
            }
            if (size > MAX_LOOKUP) {
                userNamesSection = userNames.subList(size - MAX_LOOKUP, size);
                size -= MAX_LOOKUP;
            } else {
                userNamesSection = userNames.subList(0, size);
                size = 0;
            }

            try {
                fetchedUUIDs.putAll(new UUIDFetcher(userNamesSection).call());
            } catch (Exception e) {
                // Handle 429
                if (e.getMessage().contains("429")) {
                    size += userNamesSection.size();
                    try {
                        Thread.sleep(LIMIT_PERIOD);
                    } catch (InterruptedException ex) {
                        e.printStackTrace();
                        return;
                    }
                    continue;
                }

                FactionsCaribe.getInstance().getLogger().log(Level.SEVERE, "Unable to fetch UUIDs!", e);
                return;
            }

            checkedUsers += userNamesSection.size();
            userNamesSection.clear();
            size = userNames.size();

            Misc.printProgress(checkedUsers, DatabaseManager.progressInterval, startMillis);
            if (fetchedUUIDs.size() >= BATCH_SIZE) {
                mcMMO.getDatabaseManager().saveUserUUIDs(fetchedUUIDs);
                fetchedUUIDs = new HashMap<String, UUID>();
            }
        }

        if (fetchedUUIDs.size() == 0 || mcMMO.getDatabaseManager().saveUserUUIDs(fetchedUUIDs)) {
            mcMMO.getUpgradeManager().setUpgradeCompleted(UpgradeType.ADD_UUIDS);
            FactionsCaribe.getInstance().getLogger().info("UUID upgrade completed!");
        }
    }
}
