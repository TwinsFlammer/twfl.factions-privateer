package com.redefocus.factionscaribe.listeners.general;

import com.redefocus.api.spigot.teleport.data.TeleportRequest;
import com.redefocus.api.spigot.teleport.event.UserTeleportEvent;
import com.redefocus.api.spigot.util.serialize.LocationSerialize;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author SrGutyerrez
 */
public class UserTeleportListener {
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