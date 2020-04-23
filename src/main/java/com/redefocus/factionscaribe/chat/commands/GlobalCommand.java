package com.redefocus.factionscaribe.chat.commands;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.cooldown.manager.CooldownManager;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.util.Helper;
import com.redefocus.common.shared.util.TimeFormatter;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.chat.component.ChatComponent;
import com.redefocus.factionscaribe.chat.enums.Channel;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class GlobalCommand extends CustomCommand {
    public GlobalCommand() {
        super(
                "g",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (CooldownManager.inCooldown(user, "CHAT_GLOBAL")) {
            commandSender.sendMessage(
                    String.format(
                            "§cAguarde %s para falar no chat novamente",
                            TimeFormatter.formatExtended(CooldownManager.getRemainingTime(user, "CHAT_GLOBAL"))
                    )
            );
            return;
        }

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());

        String message = Helper.toMessage(args);

        ChatComponent chatComponent = new ChatComponent(Channel.GLOBAL, caribeUser, message) {
            @Override
            public void send(CaribeUser caribeUser) {
                super.send(caribeUser);
            }
        };

        SpigotAPI.getUsers().forEach(user1 -> {
            CaribeUser caribeUser1 = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user1.getId());

            chatComponent.send(caribeUser1);
        });
    }
}
