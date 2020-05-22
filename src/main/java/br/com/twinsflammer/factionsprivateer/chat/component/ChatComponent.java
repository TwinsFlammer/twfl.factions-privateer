package br.com.twinsflammer.factionsprivateer.chat.component;

import br.com.twinsflammer.api.spigot.util.jsontext.data.JSONText;
import br.com.twinsflammer.factionsprivateer.chat.enums.Channel;
import br.com.twinsflammer.factionsprivateer.economy.manager.EconomyManager;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author SrGutyerrez
 */
public abstract class ChatComponent {
    private final JSONText jsonText;

    public ChatComponent(Channel channel, PrivateerUser privateerUser, String message) {
        JSONText jsonText;

        String tags = "";

        PrivateerUser privateerUser1 = EconomyManager.getFirstCaribeUser();

        if (privateerUser1 != null && privateerUser.isSimilar(privateerUser1))
            tags += "§2[$]";

        String factionTag = privateerUser.hasFaction() ? String.format(
                "%s[%s] ",
                privateerUser.getFactionRank() == 1 ? ChatColor.AQUA : ChatColor.GRAY,
                privateerUser.getRolePrefix() + privateerUser.getFactionTag()
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
                        .text(privateerUser.getPrefix())
                        .next()
                        .text("§f")
                        .next()
                        .text(privateerUser.getDisplayName())
                        .hoverText(
                                "§6Nick: §f" + privateerUser.getPrefix() + privateerUser.getDisplayName() +
                                "\n" +
                                "§6Facção: §f" + privateerUser.getFactionTag() + " - " + privateerUser.getFactionName() +
                                "\n" +
                                "§6KDR: §f" + privateerUser.getKdrRounded() +
                                "\n" +
                                "§6Poder: §f" + privateerUser.getPowerRounded() + "/" + privateerUser.getPowerMaxRounded() +
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
                                        privateerUser.getRolePrefix() + privateerUser.getFactionName()
                                )
                        )
                .next()
                .text("§f" + privateerUser.getDisplayName())
                .next()
                .text("§f: " + message)
                .next();
                break;
            }
            case FACTION: {
                jsonText = new JSONText()
                        .text(channel.getColor() + privateerUser.getRolePrefix())
                        .next()
                        .text("§f" + privateerUser.getDisplayName())
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

    public void send(PrivateerUser privateerUser) {
        privateerUser.sendMessage(this.jsonText);
    }

    public void send(Player player) {
        this.jsonText.send(player);
    }
}
