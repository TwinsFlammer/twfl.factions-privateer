package com.redefocus.factionscaribe.listeners.general;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * @author SrGutyerrez
 */
public class FoodLevelChangeListener implements Listener {
    @EventHandler
    public void onChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
