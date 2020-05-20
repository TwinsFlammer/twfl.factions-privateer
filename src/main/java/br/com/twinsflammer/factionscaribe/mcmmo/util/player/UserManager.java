package br.com.twinsflammer.factionscaribe.mcmmo.util.player;

import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.mcMMO;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public final class UserManager {

    private UserManager() {
    }

    /**
     * Track a new user.
     *
     * @param mcMMOPlayer the player profile to start tracking
     */
    public static void track(McMMOPlayer mcMMOPlayer) {
        mcMMOPlayer.getPlayer().setMetadata(mcMMO.playerDataKey, new FixedMetadataValue(FactionsCaribe.getInstance(), mcMMOPlayer));
    }

    /**
     * Remove a user.
     *
     * @param player The Player object
     */
    public static void remove(Player player) {
        player.removeMetadata(mcMMO.playerDataKey, FactionsCaribe.getInstance());
    }

    /**
     * Clear all users.
     */
    public static void clearAll() {
        for (Player player : FactionsCaribe.getInstance().getServer().getOnlinePlayers()) {
            remove(player);
        }
    }

    /**
     * Save all users ON THIS THREAD.
     */
    public static void saveAll() {
        ImmutableList<Player> onlinePlayers = ImmutableList.copyOf(FactionsCaribe.getInstance().getServer().getOnlinePlayers());
        mcMMO.p.debug("Saving mcMMOPlayers... (" + onlinePlayers.size() + ")");

        for (Player player : onlinePlayers) {
            getPlayer(player).getProfile().save();
        }
    }

    public static Collection<McMMOPlayer> getPlayers() {
        Collection<McMMOPlayer> playerCollection = new ArrayList<McMMOPlayer>();

        for (Player player : FactionsCaribe.getInstance().getServer().getOnlinePlayers()) {
            if (hasPlayerDataKey(player)) {
                playerCollection.add(getPlayer(player));
            }
        }

        return playerCollection;
    }

    /**
     * Get the McMMOPlayer of a player by name.
     *
     * @param playerName The name of the player whose McMMOPlayer to retrieve
     * @return the player's McMMOPlayer object
     */
    public static McMMOPlayer getPlayer(String playerName) {
        return retrieveMcMMOPlayer(playerName, false);
    }

    public static McMMOPlayer getOfflinePlayer(OfflinePlayer player) {
        if (player instanceof Player) {
            return getPlayer((Player) player);
        }

        return retrieveMcMMOPlayer(player.getName(), true);
    }

    public static McMMOPlayer getOfflinePlayer(String playerName) {
        return retrieveMcMMOPlayer(playerName, true);
    }

    public static McMMOPlayer getPlayer(Player player) {
        if (player.hasMetadata(mcMMO.playerDataKey)) {
            return (McMMOPlayer) player.getMetadata(mcMMO.playerDataKey).get(0).value();
        } else {
            return null;
        }
    }

    private static McMMOPlayer retrieveMcMMOPlayer(String playerName, boolean offlineValid) {
        Player player = FactionsCaribe.getInstance().getServer().getPlayerExact(playerName);

        if (player == null) {
            if (!offlineValid) {
                FactionsCaribe.getInstance().getLogger().warning("A valid mcMMOPlayer object could not be found for " + playerName + ".");
            }

            return null;
        }

        return getPlayer(player);
    }

    public static boolean hasPlayerDataKey(Entity entity) {
        return entity != null && entity.hasMetadata(mcMMO.playerDataKey);
    }
}
