package br.com.twinsflammer.factionsprivateer.commands.player.tpa.command.arguments;

import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.commands.player.tpa.command.TpaCommand;
import br.com.twinsflammer.factionsprivateer.commands.player.tpa.data.TpaRequest;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class TpaDenyCommand<C extends TpaCommand> extends CustomArgumentCommand<C> {
    public TpaDenyCommand() {
        super(
                0,
                "negar"
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage("§cUtilize /tpa negar <usuário>.");
            return;
        }

        String targetName = args[0];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            commandSender.sendMessage("§cEste usuário não existe.");
            return;
        }

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());
        PrivateerUser privateerUser1 = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(targetName);

        if (privateerUser1 == null) {
            commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
            return;
        }

        if (!privateerUser1.isOnline() || !SpigotAPI.getSubServersId().contains(privateerUser1.getServerId())) {
            commandSender.sendMessage("§cEste usuário não está online.");
            return;
        }

        TpaRequest tpaRequest = privateerUser.getTeleportRequestsReceived()
                .stream()
                .filter(tpaRequest1 -> tpaRequest1.getUserId().equals(privateerUser1.getId()))
                .findFirst()
                .orElse(null);

        if (tpaRequest == null) {
            commandSender.sendMessage("§cVocê não recebeu um pedido de teletransporte desse usuário.");
            return;
        }

        privateerUser.denyTpaRequest(tpaRequest);

        commandSender.sendMessage("§cVocê recusou o pedido de teletransporte.");
        privateerUser1.sendMessage("§cPedido de teletransporte rejeitado.");
    }
}
