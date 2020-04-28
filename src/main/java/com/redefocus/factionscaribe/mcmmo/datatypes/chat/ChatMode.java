package com.redefocus.factionscaribe.mcmmo.datatypes.chat;

import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;

public enum ChatMode {

    ADMIN(LocaleLoader.getString("Commands.AdminChat.On"), LocaleLoader.getString("Commands.AdminChat.Off")),
    PARTY(LocaleLoader.getString("Commands.Party.Chat.On"), LocaleLoader.getString("Commands.Party.Chat.Off"));

    private String enabledMessage;
    private String disabledMessage;

    private ChatMode(String enabledMessage, String disabledMessage) {
        this.enabledMessage = enabledMessage;
        this.disabledMessage = disabledMessage;
    }

    public String getEnabledMessage() {
        return enabledMessage;
    }

    public String getDisabledMessage() {
        return disabledMessage;
    }
}
