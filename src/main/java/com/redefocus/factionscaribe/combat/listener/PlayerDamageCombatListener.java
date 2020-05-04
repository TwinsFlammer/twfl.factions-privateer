package com.redefocus.factionscaribe.combat.listener;

import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author oNospher
 **/
public class PlayerDamageCombatListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {

            if (event.isCancelled() || event.getDamager() == null) return;

            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            if (!this.canStartCombat(damager, player))
                event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onArrowDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();

            if (projectile != null && projectile.getShooter() instanceof Player) {
                Player player = (Player) event.getEntity();
                Player damager = (Player) projectile.getShooter();

                if (!this.canStartCombat(damager, player))
                    event.setCancelled(true);
            }
        }
    }

    Boolean canStartCombat(Player damager, Player player) {
        if (player.hasMetadata("NPC") || damager.hasMetadata("NPC")) return false;

        CaribeUser caribeDamager = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(damager.getUniqueId());
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

        if (caribeDamager.canDamage(player) && caribeUser.canDamage(damager)) {
            caribeUser.setCombat(caribeDamager);
            caribeDamager.setCombat(caribeUser);
            return true;
        }

        return false;
    }
}
