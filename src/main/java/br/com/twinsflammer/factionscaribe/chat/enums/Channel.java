package br.com.twinsflammer.factionscaribe.chat.enums;

import br.com.twinsflammer.api.spigot.SpigotAPI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author SrGutyerrez
 */
@RequiredArgsConstructor
public enum Channel {
    LOCAL(
            "local",
            new String[]{
                    "l"
            },
            "l",
            ChatColor.YELLOW,
            TimeUnit.SECONDS.toMillis(3)
    ),
    GLOBAL(
            "global",
            new String[]{
                    "g"
            },
            "g",
            ChatColor.GRAY,
            TimeUnit.SECONDS.toMillis(10)
    ),
    FACTION(
            "",
            new String[]{},
            "",
            ChatColor.AQUA,
            TimeUnit.SECONDS.toMillis(3)
    ),
    ALLY(
            "",
            new String[]{},
            "§e[§a%s§e]",
            ChatColor.WHITE,
            TimeUnit.SECONDS.toMillis(3)
    );

    @Getter
    private final String name;

    private final String[] aliases;

    @Getter
    private final String prefix;
    @Getter
    private final ChatColor color;
    @Getter
    private final Long delay;

    public String getHKey() {
        return "chat_" + SpigotAPI.getRootServerId() + "_" + this.name();
    }

    public List<String> getAliases() {
        return Arrays.asList(this.aliases);
    }

    public static Channel getChannel(String name) {
        for (Channel channel : Channel.values()) {
            if (channel.toString().equalsIgnoreCase(name)
                    || channel.getName().equalsIgnoreCase(name)
                    || channel.getAliases().contains(name.toLowerCase())) return channel;
        }

        return null;
    }
}
