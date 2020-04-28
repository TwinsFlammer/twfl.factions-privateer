package com.redefocus.factionscaribe.mcmmo.runnables;

import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class MobHealthDisplayUpdaterTask extends BukkitRunnable {

    private LivingEntity target;
    private String oldName;
    private boolean oldNameVisible;

    public MobHealthDisplayUpdaterTask(LivingEntity target) {
        if (target.isValid()) {
            this.target = target;
            this.oldName = target.getMetadata(mcMMO.customNameKey).get(0).asString();
            this.oldNameVisible = target.getMetadata(mcMMO.customVisibleKey).get(0).asBoolean();
        }
    }

    @Override
    public void run() {
        if (target != null && target.isValid()) {
            target.setCustomNameVisible(oldNameVisible);
            target.setCustomName(oldName);
            target.removeMetadata(mcMMO.customNameKey, FactionsCaribe.getInstance());
            target.removeMetadata(mcMMO.customVisibleKey, FactionsCaribe.getInstance());
        }
    }
}
