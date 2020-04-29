package com.redefocus.factionscaribe.combat.runnable;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.factionscaribe.FactionsCaribe;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author oNospher
 **/
public class CombatSendActionBarRunnable implements Runnable {

    private final String IN_COMBAT_MESSAGE = "§c%s segundos para sair de combate.";
    private final String LEAVE_COMBAT_MESSAGE = "§aVocê não está mais em combate, pode deslogar.";

    @Override
    public void run() {
        SpigotAPI.getUsers().stream()
                .filter(Objects::nonNull)
                .map(user -> FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId()))
                .filter(user -> user.getCombatDuration() != 0)
                .forEach(user -> {
                    if(user.inCombat()) {
                        user.sendAction(
                                String.format(
                                        this.IN_COMBAT_MESSAGE,
                                        (int) TimeUnit.MILLISECONDS.toSeconds(user.getCombatTime())
                                )
                        );
                    } else {
                        user.setCombatDuration(0L);
                        user.sendMessage(this.LEAVE_COMBAT_MESSAGE);
                    }
                });
    }
}
