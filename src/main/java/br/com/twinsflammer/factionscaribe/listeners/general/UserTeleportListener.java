package br.com.twinsflammer.factionscaribe.listeners.general;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import br.com.twinsflammer.api.spigot.teleport.data.TeleportRequest;
import br.com.twinsflammer.api.spigot.teleport.event.UserTeleportEvent;
import br.com.twinsflammer.api.spigot.util.serialize.LocationSerialize;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author SrGutyerrez
 */
public class UserTeleportListener implements Listener {
    @EventHandler
    public void onTeleport(UserTeleportEvent event) {
        TeleportRequest teleportRequest = event.getTeleportRequest();

        Integer userId = teleportRequest.getUserId();

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(userId);

        CaribeUser.Back back = new CaribeUser.Back(
                caribeUser.getServerId(),
                LocationSerialize.toString(
                        caribeUser.getLocation()
                )
        );

        caribeUser.setBack(back);

        if (caribeUser.inCombat())
            event.setCancelled(true);
    }
}