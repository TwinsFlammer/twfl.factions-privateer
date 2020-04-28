package com.redefocus.factionscaribe.mcmmo.runnables.party;

import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.datatypes.party.Party;
import com.redefocus.factionscaribe.mcmmo.party.PartyManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class PartyAutoKickTask extends BukkitRunnable {

    private final static long KICK_TIME = 24L * 60L * 60L * 1000L * Config.getInstance().getAutoPartyKickTime();

    @Override
    public void run() {
        HashMap<OfflinePlayer, Party> toRemove = new HashMap<OfflinePlayer, Party>();
        List<UUID> processedPlayers = new ArrayList<UUID>();

        long currentTime = System.currentTimeMillis();

        for (Party party : PartyManager.getParties()) {
            for (UUID memberUniqueId : party.getMembers().keySet()) {
                OfflinePlayer member = FactionsCaribe.getInstance().getServer().getOfflinePlayer(memberUniqueId);
                boolean isProcessed = processedPlayers.contains(memberUniqueId);

                if ((!member.isOnline() && (currentTime - member.getLastPlayed() > KICK_TIME)) || isProcessed) {
                    toRemove.put(member, party);
                }

                if (!isProcessed) {
                    processedPlayers.add(memberUniqueId);
                }
            }
        }

        for (Entry<OfflinePlayer, Party> entry : toRemove.entrySet()) {
            PartyManager.removeFromParty(entry.getKey(), entry.getValue());
        }
    }
}
