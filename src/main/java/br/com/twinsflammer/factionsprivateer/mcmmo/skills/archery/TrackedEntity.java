package br.com.twinsflammer.factionsprivateer.mcmmo.skills.archery;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TrackedEntity extends BukkitRunnable {

    private LivingEntity livingEntity;
    private UUID id;
    private int arrowCount;

    protected TrackedEntity(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
        this.id = livingEntity.getUniqueId();

        this.runTaskTimer(FactionsPrivateer.getInstance(), 12000, 12000);
    }

    @Override
    public void run() {
        if (!livingEntity.isValid()) {
            Archery.removeFromTracker(this);
            this.cancel();
        }
    }

    protected LivingEntity getLivingEntity() {
        return livingEntity;
    }

    protected UUID getID() {
        return id;
    }

    protected int getArrowCount() {
        return arrowCount;
    }

    protected void incrementArrowCount() {
        arrowCount++;
    }
}
