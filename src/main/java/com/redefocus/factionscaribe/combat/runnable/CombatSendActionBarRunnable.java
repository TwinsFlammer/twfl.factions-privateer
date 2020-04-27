package com.redefocus.factionscaribe.combat.runnable;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;

import java.util.Objects;

/**
 * @author oNospher
 **/
public class CombatSendActionBarRunnable implements Runnable {

    private final String LEAVE_COMBAT_MESSAGE = "";

    @Override
    public void run() {
        SpigotAPI.getUsers().stream()
                .filter(Objects::nonNull)
                .forEach(user -> {
                    CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());
                    if(caribeUser.inCombat()) {

                    }
                });
    }
}
