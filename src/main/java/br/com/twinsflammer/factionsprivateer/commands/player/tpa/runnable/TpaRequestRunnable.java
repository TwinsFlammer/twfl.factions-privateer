package br.com.twinsflammer.factionsprivateer.commands.player.tpa.runnable;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;

/**
 * @author SrGutyerrez
 */
public class TpaRequestRunnable implements Runnable {
    @Override
    public void run() {
        FactionsPrivateer.getInstance().getCaribeUsers()
                .forEach(PrivateerUser::removeExpiredTpaRequests);
    }
}
