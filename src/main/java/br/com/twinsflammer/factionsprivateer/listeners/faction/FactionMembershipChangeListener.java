package br.com.twinsflammer.factionsprivateer.listeners.faction;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author SrGutyerrez
 */
public class FactionMembershipChangeListener implements Listener {
    @EventHandler
    public void onJoin(EventFactionsMembershipChange event) {
        MPlayer mPlayer = event.getMPlayer();

        EventFactionsMembershipChange.MembershipChangeReason membershipChangeReason = event.getReason();

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(mPlayer.getUuid());

        switch (membershipChangeReason) {
            case CREATE:
            case JOIN: {
                privateerUser.updateScoreboard(5,  "");
                privateerUser.updateScoreboard(6, "");
                privateerUser.updateScoreboard(7, "");
                privateerUser.updateScoreboard(8, "");
                privateerUser.removeScoreboard(9);
                break;
            }
            case LEAVE:
            case KICK:
            case DISBAND: {
                privateerUser.removeScoreboard(5);
                privateerUser.removeScoreboard(6);
                privateerUser.removeScoreboard(7);
                privateerUser.removeScoreboard(8);
                privateerUser.updateScoreboard(9, "");
                break;
            }
        }
    }
}
