package com.redefocus.factionscaribe.listeners.faction;

import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author SrGutyerrez
 */
public class FactionMembershipChangeListener {
    @EventHandler
    public void onJoin(EventFactionsMembershipChange event) {
        MPlayer mPlayer = event.getMPlayer();

        EventFactionsMembershipChange.MembershipChangeReason membershipChangeReason = event.getReason();

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(mPlayer.getUuid());

        switch (membershipChangeReason) {
            case CREATE:
            case JOIN: {
                caribeUser.updateScoreboard(4, "");
                caribeUser.updateScoreboard(5,  "");
                caribeUser.updateScoreboard(6, "");
                caribeUser.updateScoreboard(7, "");
                caribeUser.updateScoreboard(8, "");

                break;
            }
            case LEAVE:
            case KICK:
            case DISBAND: {
                caribeUser.removeScoreboard(4);
                caribeUser.removeScoreboard(5);
                caribeUser.removeScoreboard(6);
                caribeUser.removeScoreboard(7);
                caribeUser.removeScoreboard(8);

                break;
            }
        }
    }
}
