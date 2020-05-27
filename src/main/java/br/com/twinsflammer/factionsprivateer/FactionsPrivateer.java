package br.com.twinsflammer.factionsprivateer;

import br.com.twinsflammer.api.shared.API;
import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.TwinsPlugin;
import br.com.twinsflammer.api.spigot.restart.data.Restart;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.factionsprivateer.cash.event.CashChangeEvent;
import br.com.twinsflammer.factionsprivateer.manager.StartManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.factionsprivateer.user.factory.PrivateerUserFactory;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author SrGutyerrez
 */
public class FactionsPrivateer extends TwinsPlugin {
    @Getter
    private static FactionsPrivateer instance;

    public FactionsPrivateer() {
        FactionsPrivateer.instance = this;
    }

    @Getter
    private PrivateerUserFactory<? extends PrivateerUser> privateerUserFactory;

    @Getter
    private static final Long startTime = System.currentTimeMillis();

    private br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO mcMMO;

    @Override
    public void onEnablePlugin() {
        new StartManager();

        this.privateerUserFactory = new PrivateerUserFactory<>();

        this.mcMMO = new mcMMO();

        this.mcMMO.onEnable();

        CashChangeEvent cashChangeEvent = new CashChangeEvent();

        API.getInstance().setICashChangeEvent(cashChangeEvent);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(
                this,
                () -> {
                    if (FactionsPrivateer.startTime >= (FactionsPrivateer.startTime + TimeUnit.HOURS.toMillis(2)) && SpigotAPI.getInstance().getRestart() == null) {
                        Restart restart = new Restart(
                                System.currentTimeMillis(),
                                System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3)
                        );

                        restart.start();
                    }
                },
                0,
                20
        );
    }

    @Override
    public void onDisablePlugin() {
        this.mcMMO.onDisable();
    }

    public List<? extends PrivateerUser> getCaribeUsers() {
        return this.privateerUserFactory.getUsers();
    }

    public static void unloadUser(PrivateerUser privateerUser) {
        FactionsPrivateer.getInstance().getCaribeUsers().removeIf(caribeUser1 -> caribeUser1.isSimilar(privateerUser));
        UserManager.unloadUser(privateerUser.getId());
    }
}
