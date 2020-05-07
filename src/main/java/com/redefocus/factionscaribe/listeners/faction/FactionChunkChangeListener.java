package com.redefocus.factionscaribe.listeners.faction;

import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.massivecore.ps.PS;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author SrGutyerrez
 */
public class FactionChunkChangeListener implements Listener {
    @EventHandler
    public void onChange(EventFactionsChunksChange event) {
        Set<PS> chunkSet = event.getChunks();

        event.getNewFaction().getOnlinePlayers().forEach(player -> {
            CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

            caribeUser.updateScoreboard(5, "");
        });

        chunkSet.forEach(ps -> {
            Chunk chunk = ps.asBukkitChunk();

            Arrays.stream(chunk.getEntities())
                    .filter(entity -> entity.getType() == EntityType.PLAYER)
                    .map(entity -> (Player) entity)
                    .forEach(player -> {
                        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

                        caribeUser.updateScoreboardTitle();
                    });
        });
    }
}
