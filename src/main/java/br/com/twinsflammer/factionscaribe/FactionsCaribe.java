package br.com.twinsflammer.factionscaribe;

import br.com.twinsflammer.factionscaribe.cash.event.CashChangeEvent;
import br.com.twinsflammer.factionscaribe.manager.StartManager;
import br.com.twinsflammer.factionscaribe.mcmmo.mcMMO;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import br.com.twinsflammer.factionscaribe.user.factory.CaribeUserFactory;
import br.com.twinsflammer.api.shared.API;
import br.com.twinsflammer.api.spigot.TwinsPlugin;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import lombok.Getter;

import java.util.List;

/**
 * @author SrGutyerrez
 */
public class FactionsCaribe extends TwinsPlugin {
    @Getter
    private static FactionsCaribe instance;

    public FactionsCaribe() {
        FactionsCaribe.instance = this;
    }

    @Getter
    private CaribeUserFactory<? extends CaribeUser> caribeUserFactory;

    @Getter
    private static final Long startTime = System.currentTimeMillis();

    private br.com.twinsflammer.factionscaribe.mcmmo.mcMMO mcMMO;

    @Override
    public void onEnablePlugin() {
        new StartManager();

        this.caribeUserFactory = new CaribeUserFactory<>();

        this.mcMMO = new mcMMO();

        this.mcMMO.onEnable();

        CashChangeEvent cashChangeEvent = new CashChangeEvent();

        API.getInstance().setICashChangeEvent(cashChangeEvent);
    }

    @Override
    public void onDisablePlugin() {
        this.mcMMO.onDisable();
    }

    public List<? extends CaribeUser> getCaribeUsers() {
        return this.caribeUserFactory.getUsers();
    }

    public static void unloadUser(CaribeUser caribeUser) {
        FactionsCaribe.getInstance().getCaribeUsers().removeIf(caribeUser1 -> caribeUser1.isSimilar(caribeUser));
        UserManager.unloadUser(caribeUser.getId());
    }
}
