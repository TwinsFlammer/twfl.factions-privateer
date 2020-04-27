package com.redefocus.factionscaribe.chat.enums;

import com.redefocus.api.spigot.SpigotAPI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

import java.util.concurrent.TimeUnit;

/**
 * @author SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public enum Channel {
    LOCAL(
            "l",
            ChatColor.YELLOW,
            TimeUnit.SECONDS.toMillis(3)
    ),
    GLOBAL(
            "g",
            ChatColor.GRAY,
            TimeUnit.SECONDS.toMillis(10)
    ),
    FACTION(
            "",
            ChatColor.AQUA,
            TimeUnit.SECONDS.toMillis(3)
    ),
    ALLY(
            "§e[§a%s§e]",
            ChatColor.WHITE,
            TimeUnit.SECONDS.toMillis(3)
    );

    private final String prefix;
    private final ChatColor color;
    private final Long delay;

    public String getHKey() {
        return "chat_" + SpigotAPI.getRootServerId() + "_" + this.name();
    }
}
