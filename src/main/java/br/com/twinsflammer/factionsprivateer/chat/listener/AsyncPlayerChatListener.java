package br.com.twinsflammer.factionsprivateer.chat.listener;

import br.com.twinsflammer.api.spigot.util.action.data.CustomAction;
import br.com.twinsflammer.common.shared.cooldown.manager.CooldownManager;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.preference.Preference;
import br.com.twinsflammer.common.shared.util.TimeFormatter;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.chat.commands.chat.factory.ChatFactory;
import br.com.twinsflammer.factionsprivateer.chat.component.ChatComponent;
import br.com.twinsflammer.factionsprivateer.chat.enums.Channel;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMessage(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();

        User user = UserManager.getUser(player.getUniqueId());

        event.setCancelled(true);

        if (CooldownManager.inCooldown(user, AsyncPlayerChatListener.OBJECT_NAME)) {
            new CustomAction()
                    .text(
                            String.format(
                                    "§cAguarde %s para falar no chat novamente.",
                                    TimeFormatter.formatExtended(CooldownManager.getRemainingTime(user, AsyncPlayerChatListener.OBJECT_NAME))
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

        ChatFactory<Channel> chatFactory = new ChatFactory<>();

        if (!chatFactory.isChannelActive(Channel.LOCAL)) {
            player.sendMessage("§cEste canal não está ativo no momento.");
            return;
        }

        if (players.stream().filter(player1 -> {
            PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player1.getUniqueId());

            return !privateerUser.isInvisible();
        }).count() <= 0) {
            new CustomAction()
                    .text(
                            "§cNão há ninguém por perto para você conversar :("
                    )
                    .getSpigot()
                    .send(player);
            return;
        }

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getUniqueId());

        CooldownManager.startCooldown(
                user,
                TimeUnit.SECONDS.toMillis(
                        privateerUser.getLocalChatCooldown()
                ),
                AsyncPlayerChatListener.OBJECT_NAME
        );

        String message = event.getMessage();

        ChatComponent chatComponent = new ChatComponent(Channel.LOCAL, privateerUser, message) { };

        players.forEach(chatComponent::send);

        chatComponent.send(player);
    }
}
