package br.com.twinsflammer.factionsprivateer.combat.listener;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;

/**
 * @author oNospher
 **/
public class PlayerDamageCombatListener implements Listener {
    private final EntityType[] ALLOWED_TYPES = {
            EntityType.ARROW
    };

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
                if (!Arrays.asList(this.ALLOWED_TYPES).contains(projectile.getType())) return;

                Player player = (Player) event.getEntity();
                Player damager = (Player) projectile.getShooter();

                if (!this.canStartCombat(damager, player))
                    event.setCancelled(true);
            }
        }
    }

    Boolean canStartCombat(Player damager, Player player) {
        if (player.hasMetadata("NPC") || damager.hasMetadata("NPC")) return false;

        PrivateerUser caribeDamager = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(damager.getUniqueId());
        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

        if (caribeDamager.canDamage(player) && privateerUser.canDamage(damager)) {
            privateerUser.setCombat(caribeDamager);
            caribeDamager.setCombat(privateerUser);
            return true;
        }

        return false;
    }
}
