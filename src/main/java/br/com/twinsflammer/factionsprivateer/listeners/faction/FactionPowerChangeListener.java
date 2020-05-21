package br.com.twinsflammer.factionsprivateer.listeners.faction;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsPowerChange;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author SrGutyerrez
 */
public class FactionPowerChangeListener implements Listener {
    @EventHandler
    public void onChange(EventFactionsPowerChange event) {
        MPlayer mPlayer = event.getMPlayer();

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(mPlayer.getUuid());

        privateerUser.updateScoreboard(10, mPlayer.getPowerRounded() + "/" + mPlayer.getPowerMaxRounded());
    }
}
