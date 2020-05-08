package com.redefocus.factionscaribe.chat.component;

import com.redefocus.api.spigot.util.jsontext.data.JSONText;
import com.redefocus.factionscaribe.chat.enums.Channel;
import com.redefocus.factionscaribe.economy.manager.EconomyManager;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author SrGutyerrez
 */
public abstract class ChatComponent {
    private final JSONText jsonText;

    public ChatComponent(Channel channel, CaribeUser caribeUser, String message) {
        JSONText jsonText;

        String tags = "";

        CaribeUser caribeUser1 = EconomyManager.getFirstCaribeUser();

        if (caribeUser1 != null && caribeUser.isSimilar(caribeUser1))
            tags += "§2[$]";

        String factionTag = caribeUser.hasFaction() ? String.format(
                "%s[%s] ",
                caribeUser.getFactionRank() == 1 ? ChatColor.AQUA : ChatColor.GRAY,
                caribeUser.getFactionTag()
        ) : "";

        switch (channel) {
            case GLOBAL:
            case LOCAL: {
                jsonText = new JSONText()
                        .text(channel.getColor() + "[" + channel.getPrefix() + "] ")
                        .next()
                        .text(tags)
                        .next()
                        .text(factionTag)
                        .next()
                        .text(caribeUser.getPrefix())
                        .next()
                        .text("§f")
                        .next()
                        .text(caribeUser.getDisplayName())
                        .hoverText(
                                "§6Nick: §f" + caribeUser.getPrefix() + caribeUser.getDisplayName() +
                                "\n" +
                                "§6Facção: §f" + caribeUser.getFactionTag() + "- " + caribeUser.getFactionName() +
                                "\n" +
                                "§6KDR: §f" + caribeUser.getKdrRounded() +
                                "\n" +
                                "§6Poder: §f" + caribeUser.getPowerRounded() + "/" + caribeUser.getPowerMaxRounded() +
                                "\n" +
                                "§6Coins: §f" + 0.0
                        )
                        .next()
                        .text(channel.getColor() + ": ")
                        .next()
                        .text(channel.getColor() + message)
                        .next();
                break;
            }
            case ALLY: {
                jsonText = new JSONText()
                        .text(
                                String.format(
                                        channel.getPrefix(),
                                        caribeUser.getRolePrefix() + caribeUser.getFactionName()
                                )
                        )
                .next()
                .text("§f" + caribeUser.getDisplayName())
                .next()
                .text("§f: " + message)
                .next();
                break;
            }
            case FACTION: {
                jsonText = new JSONText()
                        .text(channel.getColor() + caribeUser.getRolePrefix())
                        .next()
                        .text("§f" + caribeUser.getDisplayName())
                        .next()
                        .text(": §b" + message)
                        .next();
                break;
            }
            default:
                jsonText = null;
        }

        this.jsonText = jsonText;
    }

    public void send(CaribeUser caribeUser) {
        caribeUser.sendMessage(this.jsonText);
    }

    public void send(Player player) {
        this.jsonText.send(player);
    }
}
