package br.com.twinsflammer.factionscaribe.listeners.faction;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
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

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(mPlayer.getUuid());

        caribeUser.updateScoreboard(10, mPlayer.getPowerRounded() + "/" + mPlayer.getPowerMaxRounded());
    }
}
