package com.redefocus.factionscaribe;

import com.redefocus.api.spigot.FocusPlugin;
import com.redefocus.factionscaribe.manager.StartManager;
import lombok.Getter;

public class FactionsCaribe extends FocusPlugin {
    @Getter
    private static FactionsCaribe instance;

    public FactionsCaribe() {
        FactionsCaribe.instance = this;
    }

    @Override
    public void onEnablePlugin() {
        new StartManager();
    }

    @Override
    public void onDisablePlugin() {

    }
}
