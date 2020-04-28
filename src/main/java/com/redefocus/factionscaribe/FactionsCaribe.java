package com.redefocus.factionscaribe;

import com.redefocus.api.spigot.FocusPlugin;
import com.redefocus.factionscaribe.manager.StartManager;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import com.redefocus.factionscaribe.user.factory.CaribeUserFactory;
import lombok.Getter;

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
    }

    @Override
    public void onDisablePlugin() {
        this.mcMMO.onDisable();
    }
}
