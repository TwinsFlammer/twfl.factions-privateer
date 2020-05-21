package br.com.twinsflammer.factionsprivateer.listeners.faction;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Set;

/**
 * @author SrGutyerrez
 */
public class FactionChunkChangeListener implements Listener {
    @EventHandler
    public void onChange(EventFactionsChunksChange event) {
        Set<PS> chunkSet = event.getChunks();

        event.getNewFaction().getOnlinePlayers().forEach(player -> {
            PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

            privateerUser.updateScoreboard(5, "");
        });

        chunkSet.forEach(ps -> {
            Chunk chunk = ps.asBukkitChunk();

            Arrays.stream(chunk.getEntities())
                    .filter(entity -> entity.getType() == EntityType.PLAYER)
                    .map(entity -> (Player) entity)
                    .forEach(player -> {
                        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

                        privateerUser.updateScoreboardTitle();
                    });
        });
    }
}
