package com.redefocus.factionscaribe.combat.listener;

import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author oNospher
 **/
public class PlayerDamageCombatListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {

            if(event.isCancelled() || event.getDamager() == null) return;

            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            if(player.hasMetadata("NPC") || damager.hasMetadata("NPC")) return;

            CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());
            CaribeUser caribeDamager = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(damager.getUniqueId());

            caribeUser.setCombat(caribeDamager);
            caribeDamager.setCombat(caribeUser);
        }
    }
}
