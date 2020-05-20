package br.com.twinsflammer.factionscaribe.chat.commands;

import br.com.twinsflammer.factionscaribe.chat.commands.chat.factory.ChatFactory;
import br.com.twinsflammer.factionscaribe.chat.component.ChatComponent;
import br.com.twinsflammer.factionscaribe.chat.enums.Channel;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.spigot.util.action.data.CustomAction;
import br.com.twinsflammer.common.shared.cooldown.manager.CooldownManager;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.preference.Preference;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.common.shared.util.TimeFormatter;
import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

/**
 * @author SrGutyerrez
 */
public class GlobalCommand extends CustomCommand {
    private static final String OBJECT_NAME = "CHAT_GLOBAL";

    public GlobalCommand() {
        super(
                "g",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        Player player = (Player) commandSender;

        if (CooldownManager.inCooldown(user, GlobalCommand.OBJECT_NAME)) {
            new CustomAction()
                    .text(
                            String.format(
                                    "§cAguarde %s para falar no chat novamente",
                                    TimeFormatter.formatExtended(CooldownManager.getRemainingTime(user, GlobalCommand.OBJECT_NAME))
                            )
                    )
                    .getSpigot()
                    .send(player);
            return;
        }

        if (args.length == 0) {
            commandSender.sendMessage("§cUtilize /g <mensagem>.");
            return;
        }

        ChatFactory<Channel> chatFactory = new ChatFactory<>();

        if (!chatFactory.isChannelActive(Channel.GLOBAL)) {
            commandSender.sendMessage("§cEste canal não está ativo no momento.");
            return;
        }

        if ((user.getFirstLogin() + TimeUnit.HOURS.toMillis(5)) >= System.currentTimeMillis()) {
            new CustomAction()
                    .text(
                            String.format(
                                    "§cVocê deve aguardar %s para poder falar no chat global.",
                                    TimeFormatter.formatExtended(
                                            (user.getFirstLogin() + TimeUnit.HOURS.toMillis(5)) - System.currentTimeMillis()
                                    )
                            )
                    )
                    .getSpigot()
                    .send(player);
            return;
        }

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());

        CooldownManager.startCooldown(
                user,
                TimeUnit.SECONDS.toMillis(
                        caribeUser.getGlobalChatCooldown()
                ),
                GlobalCommand.OBJECT_NAME
        );

        String message = Helper.toMessage(args);

        if (caribeUser.getLastMessage() != null && caribeUser.getLastMessage().equalsIgnoreCase(message)) {
            commandSender.sendMessage("§cVocê não pode enviar uma mensagem tão similar a sua anterior.");
            return;
        }

        caribeUser.setLastMessage(message);

        ChatComponent chatComponent = new ChatComponent(Channel.GLOBAL, caribeUser, message) { };

        SpigotAPI.getUsers()
                .stream()
                .filter(user1 -> user1.isEnabled(Preference.CHAT_GLOBAL))
                .filter(user1 -> !user1.isIgnoring(user))
                .forEach(user1 -> {
                    CaribeUser caribeUser1 = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user1.getId());

                    chatComponent.send(caribeUser1);
                });
    }
}
