package br.com.twinsflammer.factionscaribe.combat.runnable;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import br.com.twinsflammer.api.spigot.util.action.data.CustomAction;
import br.com.twinsflammer.common.shared.util.TimeFormatter;
import org.bukkit.entity.Player;

/**
 * @author oNospher
 **/
public class CombatSendActionBarRunnable implements Runnable {
    private final String IN_COMBAT_MESSAGE = "§c%s segundos para sair de combate.";
    private final String LEAVE_COMBAT_MESSAGE = "§aVocê não está mais em combate, pode deslogar.";

    @Override
    public void run() {
        FactionsCaribe.getInstance().getCaribeUsers()
                .stream()
                .filter(CaribeUser::inCombat)
                .forEach(caribeUser -> {
                    Player player = caribeUser.getPlayer();

                    if (player != null) {

                        new CustomAction()
                                .text(
                                        String.format(
                                                this.IN_COMBAT_MESSAGE,
                                                TimeFormatter.formatMinimized(
                                                        caribeUser.getCombatDuration() - System.currentTimeMillis()
                                                )
                                        )
                                )
                                .spigot()
                                .send(caribeUser.getPlayer());

                        if (caribeUser.getCombatDuration() <= System.currentTimeMillis()) {
                            caribeUser.setCombatDuration(0L);

                            player.sendMessage(this.LEAVE_COMBAT_MESSAGE);
                        }
                    }
                });
    }
}
