package com.redefocus.factionscaribe.chat.listener;

import com.redefocus.api.spigot.util.action.data.CustomAction;
import com.redefocus.common.shared.cooldown.manager.CooldownManager;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.common.shared.preference.Preference;
import com.redefocus.common.shared.util.TimeFormatter;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.chat.component.ChatComponent;
import com.redefocus.factionscaribe.chat.enums.Channel;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author SrGutyerrez
 */
public class AsyncPlayerChatListener implements Listener {
    private static final String OBJECT_NAME = "CHAT_LOCAL";

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        User user = UserManager.getUser(player.getUniqueId());

        event.setCancelled(true);

        if (CooldownManager.inCooldown(user, AsyncPlayerChatListener.OBJECT_NAME)) {
            new CustomAction()
                    .text(
                            String.format(
                                    "§cAguarde %s para falar no chat novamente.",
                                    TimeFormatter.formatExtended(CooldownManager.getRemainingTime(user, "CHAT_LOCAL"))
                            )
                    )
                    .getSpigot()
                    .send(player);
            return;
        }

        List<Player> players = player.getNearbyEntities(70, 70, 70)
                .stream()
                .filter(entity -> !entity.isDead())
                .filter(entity -> !entity.hasMetadata("NPC"))
                .filter(entity -> entity.getType().equals(EntityType.PLAYER))
                .map(entity -> (Player) entity)
                .filter(player1 -> {
                    User user1 = UserManager.getUser(player1.getUniqueId());

                    return user1.isEnabled(Preference.CHAT_LOCAL) && !user1.isIgnoring(user);
                })
                .collect(Collectors.toList());

        if (players.isEmpty()) {
            new CustomAction()
                    .text(
                            "§cNão há ninguém por perto para você conversar :("
                    )
                    .getSpigot()
                    .send(player);
            return;
        }

        CooldownManager.startCooldown(
                user,
                TimeUnit.SECONDS.toMillis(3),
                AsyncPlayerChatListener.OBJECT_NAME
        );

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getUniqueId());

        String message = event.getMessage();

        ChatComponent chatComponent = new ChatComponent(Channel.LOCAL, caribeUser, message) {
            @Override
            public void send(CaribeUser caribeUser) {
                super.send(caribeUser);
            }
        };

        players.forEach(player1 -> {
            CaribeUser caribeUser1 = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

            chatComponent.send(caribeUser1);
        });
    }
}
