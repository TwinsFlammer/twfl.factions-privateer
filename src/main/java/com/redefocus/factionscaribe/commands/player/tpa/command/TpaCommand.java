package com.redefocus.factionscaribe.commands.player.tpa.command;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.api.spigot.util.jsontext.data.JSONText;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.commands.player.tpa.command.arguments.TpaAcceptCommand;
import com.redefocus.factionscaribe.commands.player.tpa.command.arguments.TpaCancelCommand;
import com.redefocus.factionscaribe.commands.player.tpa.command.arguments.TpaDenyCommand;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class TpaCommand extends CustomCommand {
    public TpaCommand() {
        super(
                "tpa",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );

        this.addArgument(
                new TpaAcceptCommand(),
                new TpaCancelCommand(),
                new TpaDenyCommand()
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage("§cUtilize /tpa <usuário>.");
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

        JSONText jsonText = new JSONText()
                .text(
                        String.format(
                                "§ePedido de teletransporte enviado para §7%s %s",
                                caribeUser1.hasFaction() ? "[" + caribeUser1.getRolePrefix() + caribeUser1.getFactionTag() + "]" : "",
                                caribeUser1.getPrefix() + caribeUser1.getDisplayName()
                        )
                )
                .next()
                .text("§eClique ")
                .next()
                .text("§c§lAQUI")
                .execute("/tpa cancelar " + caribeUser1.getName())
                .next()
                .text("§epara cancelar este pedido.")
                .next(),
                jsonText1 = new JSONText()
                        .text(
                                String.format(
                                        "§7%s %s §eestá pedindo para teletransportar até você.",
                                        caribeUser.hasFaction() ? "[" + caribeUser.getRolePrefix() + caribeUser.getFactionTag() + "]" : "",
                                        caribeUser.getPrefix() + caribeUser.getDisplayName()
                                )
                        )
                        .next()
                        .text("§eClique ")
                        .next()
                        .text("§a§lAQUI")
                        .execute("/tpa aceitar " + caribeUser.getName())
                        .next()
                        .text("§epara aceitar e ")
                        .next()
                        .text("§c§AQUI")
                        .execute("/tpa negar " + caribeUser.getName())
                        .next()
                        .text("§epara negar.")
                        .next();

        jsonText.send(commandSender);
        caribeUser1.sendMessage(jsonText1);

        caribeUser.sendTpaRequest(caribeUser1);
    }
}
