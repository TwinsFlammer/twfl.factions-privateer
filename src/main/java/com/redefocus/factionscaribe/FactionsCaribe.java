package com.redefocus.factionscaribe;

import com.redefocus.api.shared.API;
import com.redefocus.api.spigot.FocusPlugin;
import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.factionscaribe.cash.event.CashChangeEvent;
import com.redefocus.factionscaribe.manager.StartManager;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import com.redefocus.factionscaribe.user.factory.CaribeUserFactory;
import lombok.Getter;

import java.util.List;

/**
 * @author SrGutyerrez
 */
public class FactionsCaribe extends FocusPlugin {
    @Getter
    private static FactionsCaribe instance;

    public FactionsCaribe() {
        FactionsCaribe.instance = this;
    }

    @Getter
    private CaribeUserFactory<? extends CaribeUser> caribeUserFactory;

    @Getter
    private static final Long startTime = System.currentTimeMillis();

    private mcMMO mcMMO;

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
