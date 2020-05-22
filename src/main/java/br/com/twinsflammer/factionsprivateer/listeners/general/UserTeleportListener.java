package br.com.twinsflammer.factionsprivateer.listeners.general;

import br.com.twinsflammer.api.spigot.teleport.data.TeleportRequest;
import br.com.twinsflammer.api.spigot.teleport.event.UserTeleportEvent;
import br.com.twinsflammer.api.spigot.util.serialize.LocationSerialize;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
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

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(userId);

        PrivateerUser.Back back = new PrivateerUser.Back(
                privateerUser.getServerId(),
                LocationSerialize.toString(
                        privateerUser.getLocation()
                )
        );

        privateerUser.setBack(back);

        if (privateerUser.inCombat())
            event.setCancelled(true);
    }
}