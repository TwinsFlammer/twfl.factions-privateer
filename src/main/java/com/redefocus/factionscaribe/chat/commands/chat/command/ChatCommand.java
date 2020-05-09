package com.redefocus.factionscaribe.chat.commands.chat.command;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.chat.commands.chat.factory.ChatFactory;
import com.redefocus.factionscaribe.chat.enums.Channel;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author SrGutyerrez
 */
public class ChatCommand extends CustomCommand {
    private final String[] ALLOWED_ACTIONS = {
            "on",
            "off"
    };

    public ChatCommand() {
        super(
                "chat",
                CommandRestriction.IN_GAME,
                GroupNames.DIRECTOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length != 2) {
            commandSender.sendMessage("§cUtilize /chat <on/off> <canal>.");
            return;
        }

        String action = args[0];

        if (!Arrays.asList(this.ALLOWED_ACTIONS).contains(action)) {
            commandSender.sendMessage("§cVocê inseriu uma ação inválida.");
            return;
        }

        Channel channel = Channel.getChannel(args[1]);

        if (channel == null) {
            commandSender.sendMessage("§cEste canal não existe.");
            return;
        }

        ChatFactory<Channel> chatFactory = new ChatFactory<>();

        Boolean newStatus = action.equals("on");

        chatFactory.changeChannelStatus(
                channel,
                newStatus
        );

        commandSender.sendMessage(
                String.format(
                        "§aO canal de chat %s foi %s.",
                        channel.getName(),
                        newStatus ? "ativado" : "desativado"
                )
        );
    }
}
