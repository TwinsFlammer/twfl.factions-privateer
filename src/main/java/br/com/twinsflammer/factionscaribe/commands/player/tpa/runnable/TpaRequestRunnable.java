package br.com.twinsflammer.factionscaribe.commands.player.tpa.runnable;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;

/**
 * @author SrGutyerrez
 */
public class TpaRequestRunnable implements Runnable {
    @Override
    public void run() {
        FactionsCaribe.getInstance().getCaribeUsers()
                .forEach(CaribeUser::removeExpiredTpaRequests);
    }
}
