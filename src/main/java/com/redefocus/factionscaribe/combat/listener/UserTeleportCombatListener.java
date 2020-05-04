package com.redefocus.factionscaribe.combat.listener;

import com.redefocus.api.spigot.teleport.data.TeleportRequest;
import com.redefocus.api.spigot.teleport.event.UserTeleportEvent;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author oNospher
 **/
public class UserTeleportCombatListener implements Listener {
    @EventHandler
    public void onTeleport(UserTeleportEvent event) {
        TeleportRequest teleportRequest = event.getTeleportRequest();

        Integer userId = teleportRequest.getUserId();

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(userId);

        if (caribeUser.inCombat())
            event.setCancelled(true);
    }
}
