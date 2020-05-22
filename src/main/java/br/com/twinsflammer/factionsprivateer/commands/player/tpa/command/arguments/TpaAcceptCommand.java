package br.com.twinsflammer.factionsprivateer.commands.player.tpa.command.arguments;

import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.commands.CustomArgumentCommand;
import br.com.twinsflammer.api.spigot.util.jsontext.data.JSONText;
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
            commandSender.sendMessage("§cUtilize /tpa aceitar <usuário>.");
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
            commandSender.sendMessage("§cEste usuário não lhe enviou pedido de teleporte.");
            return;
        }

        privateerUser.acceptTpaRequest(tpaRequest);

        commandSender.sendMessage("§aPedido de teletransporte aceito!");

        JSONText jsonText = new JSONText()
                .text(
                        String.format(
                                "§7%s%s §aaceitou o pedido de teletransporte.",
                                privateerUser.hasFaction() ? "[" + privateerUser.getRolePrefix() + privateerUser.getFactionTag() + "] " : "",
                                privateerUser.getPrefix() + privateerUser.getDisplayName()
                        )
                )
                .next()
                .text("\n")
                .next()
                .text(
                        String.format(
                                "§aTeletransportando para §7%s%s",
                                privateerUser.hasFaction() ? "[" + privateerUser.getRolePrefix() + privateerUser.getFactionTag() + "] " : "",
                                privateerUser.getPrefix() + privateerUser.getDisplayName()

                        )
                )
                .next();

        privateerUser1.sendMessage(jsonText);
    }
}
