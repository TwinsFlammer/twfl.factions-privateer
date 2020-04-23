package com.redefocus.factionscaribe.chat.component;

import com.redefocus.api.spigot.util.jsontext.data.JSONText;
import com.redefocus.factionscaribe.chat.enums.Channel;
import com.redefocus.factionscaribe.user.data.CaribeUser;

/**
 * @author SrGutyerrez
 */
public abstract class ChatComponent {
    private final JSONText jsonText;

    public ChatComponent(Channel channel, CaribeUser user, String message) {
        JSONText jsonText;

        switch (channel) {
            case GLOBAL:
            case LOCAL: {
                jsonText = new JSONText()
                        .text(channel.getColor() + "[" + channel.getPrefix() + "]")
                        .next()
                        .text(user.getPrefix())
                        .next()
                        .text("§f")
                        .next()
                        .text(user.getDisplayName())
                        .hoverText("")
                        .next()
                        .text(channel.getColor() + ": ")
                        .next()
                        .text(message);
                break;
            }
            case ALLY: {
                jsonText = new JSONText()
                        .text(
                                String.format(
                                        channel.getPrefix(),
                                        user.getRolePrefix() + user.getFactionName()
                                )
                        )
                .next()
                .text("§f" + user.getDisplayName())
                .next()
                .text("§f: " + message);
                break;
            }
            case FACTION: {
                jsonText = new JSONText()
                        .text(channel.getColor() + user.getRolePrefix())
                        .next()
                        .text("§f" + user.getDisplayName())
                        .next()
                        .text(": §b" + message);
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
}
