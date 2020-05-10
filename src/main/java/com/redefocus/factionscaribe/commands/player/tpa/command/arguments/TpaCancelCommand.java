package com.redefocus.factionscaribe.commands.player.tpa.command.arguments;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.api.spigot.commands.CustomArgumentCommand;
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
public class TpaCancelCommand<C extends TpaCommand> extends CustomArgumentCommand<C> {
    public TpaCancelCommand() {
        super(
                0,
                "cancelar"
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage("§cUtilize /tpa cancelar <usuário>.");
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

        TpaRequest tpaRequest = caribeUser.getTeleportRequestsSent()
                .stream()
                .filter(tpaRequest1 -> tpaRequest1.getTargetId().equals(caribeUser1.getId()))
                .findFirst()
                .orElse(null);

        if (tpaRequest == null) {
            commandSender.sendMessage("§cVocê não enviou um pedido de teletransporte para esse usuário.");
            return;
        }

        caribeUser.cancelTpaRequest(tpaRequest);

        commandSender.sendMessage("§cPedido de teletransporte cancelado.");
    }
}
