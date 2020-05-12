package com.redefocus.factionscaribe.listeners.player;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author SrGutyerrez
 */
public class PlayerChunkChangeListener implements Listener {
    @EventHandler
    public void onChange(PlayerMoveEvent event) {
        Location fromLocation = event.getFrom();
        Location toLocation = event.getTo();

        Chunk fromChunk = fromLocation.getChunk();
        Chunk toChunk = toLocation.getChunk();

        if (!fromChunk.equals(toChunk)) {
            Player player = event.getPlayer();

            Faction faction = BoardColl.get().getFactionAt(PS.valueOf(toChunk));

            CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

            caribeUser.updateScoreboardTitle(faction);
        }
    }
}
