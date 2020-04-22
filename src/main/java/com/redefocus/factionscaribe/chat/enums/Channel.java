package com.redefocus.factionscaribe.chat.enums;

import com.redefocus.api.spigot.SpigotAPI;
import lombok.RequiredArgsConstructor;

/**
 * @author SrGutyerrez
 */
@RequiredArgsConstructor
public enum Channel {
    LOCAL(
            "L"
    ),
    GLOBAL(
            "G"
    ),
    FACTION(
            ""
    ),
    ALLY(
            "A"
    );

    private final String prefix;

    public String getHKey() {
        return "chat_" + SpigotAPI.getRootServerId() + "_" + this.name();
    }
}
