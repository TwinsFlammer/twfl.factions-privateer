package com.redefocus.factionscaribe.commands.player.tpa.runnable;

import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;

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
