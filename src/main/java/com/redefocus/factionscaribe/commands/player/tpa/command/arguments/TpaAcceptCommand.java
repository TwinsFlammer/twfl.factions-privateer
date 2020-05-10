package com.redefocus.factionscaribe.commands.player.tpa.command.arguments;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.api.spigot.commands.CustomArgumentCommand;
import com.redefocus.api.spigot.util.jsontext.data.JSONText;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.commands.player.tpa.command.TpaCommand;
import com.redefocus.factionscaribe.commands.player.tpa.data.TpaRequest;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class TpaAcceptCommand<C extends TpaCommand> extends CustomArgumentCommand<C> {
    public TpaAcceptCommand() {
        super(
                0,
                "aceitar"
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage(this.getUsage("<usuário>"));
            return;
        }

        String targetName = args[0];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            commandSender.sendMessage("§cEste usuário não existe.");
            return;
        }

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());
        CaribeUser caribeUser1 = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(targetName);

        if (caribeUser1 == null) {
            commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
            return;
        }

        if (!caribeUser1.isOnline() || !SpigotAPI.getSubServersId().contains(caribeUser1.getServerId())) {
            commandSender.sendMessage("§cEste usuário não está online.");
            return;
        }

        TpaRequest tpaRequest = caribeUser.getTeleportRequestsReceived()
                .stream()
                .filter(tpaRequest1 -> tpaRequest1.getUserId().equals(caribeUser1.getId()))
                .findFirst()
                .orElse(null);

        if (tpaRequest == null) {
            commandSender.sendMessage("§cEste usuário não lhe enviou pedido de teleporte.");
            return;
        }

        caribeUser.acceptTpaRequest(tpaRequest);

        commandSender.sendMessage("§aPedido de teletransporte aceito!");

        JSONText jsonText = new JSONText()
                .text(
                        String.format(
                                "§7%s%s §caceitou o pedido de teletransporte.",
                                caribeUser.hasFaction() ? "[" + caribeUser.getRolePrefix() + caribeUser1.getFactionTag() + "] " : "",
                                caribeUser.getPrefix() + caribeUser.getDisplayName()
                        )
                )
                .next()
                .text("\n")
                .next()
                .text(
                        String.format(
                                "§aTeletransportando para §7%s%s",
                                caribeUser.hasFaction() ? "[" + caribeUser.getRolePrefix() + caribeUser1.getFactionTag() + "] " : "",
                                caribeUser.getPrefix() + caribeUser.getDisplayName()

                        )
                )
                .next();

        caribeUser1.sendMessage(jsonText);
    }
}
