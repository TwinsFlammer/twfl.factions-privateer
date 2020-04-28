package com.redefocus.factionscaribe.mcmmo.skills.taming;

import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.util.Misc;
import com.redefocus.factionscaribe.mcmmo.util.skills.CombatUtils;
import com.redefocus.factionscaribe.mcmmo.util.skills.ParticleEffectUtils;
import java.util.UUID;

import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

public class TrackedTamingEntity extends BukkitRunnable {

    private LivingEntity livingEntity;
    private UUID id;
    private int length;

    protected TrackedTamingEntity(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
        this.id = livingEntity.getUniqueId();

        int tamingCOTWLength = Config.getInstance().getTamingCOTWLength(livingEntity.getType());

        if (tamingCOTWLength > 0) {
            this.length = tamingCOTWLength * Misc.TICK_CONVERSION_FACTOR;
            this.runTaskLater(FactionsCaribe.getInstance(), length);
        }
    }

    @Override
    public void run() {
        if (livingEntity.isValid()) {
            Location location = livingEntity.getLocation();
            location.getWorld().playSound(location, Sound.FIZZ, 0.8F, 0.8F);
            ParticleEffectUtils.playCallOfTheWildEffect(livingEntity);
            CombatUtils.dealDamage(livingEntity, livingEntity.getMaxHealth(), DamageCause.SUICIDE, livingEntity);
        }

        TamingManager.removeFromTracker(this);
        this.cancel();
    }

    protected LivingEntity getLivingEntity() {
        return livingEntity;
    }

    protected UUID getID() {
        return id;
    }
}
