package com.redefocus.factionscaribe.commands.player.tpa.command;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.api.spigot.util.jsontext.data.JSONText;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.common.shared.util.TimeFormatter;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.commands.player.tpa.command.arguments.TpaAcceptCommand;
import com.redefocus.factionscaribe.commands.player.tpa.command.arguments.TpaCancelCommand;
import com.redefocus.factionscaribe.commands.player.tpa.command.arguments.TpaDenyCommand;
import com.redefocus.factionscaribe.commands.player.tpa.data.TpaRequest;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        if (caribeUser.isSimilar(caribeUser1)) {
            commandSender.sendMessage("§cVocê não pode pedir para teletransporta-se a si mesmo.");
            return;
        }

        if (!caribeUser1.isOnline() || !SpigotAPI.getSubServersId().contains(caribeUser1.getServerId())) {
            commandSender.sendMessage("§cEste usuário não está online.");
            return;
        }

        TpaRequest tpaRequest = caribeUser.getTeleportRequestsSent()
                .stream()
                .filter(tpaRequest1 -> {
                    Boolean asd = tpaRequest1.getTargetId().equals(caribeUser1.getId());

                    System.out.println("id: " + asd);

                    return asd;
                })
                .filter(tpaRequest1 -> {
                    Boolean asd = tpaRequest1.hasExpired();

                    System.out.println("expirou: " + asd);

                    return !asd;
                })
                .findFirst()
                .orElse(null);

        if (tpaRequest != null) {
            commandSender.sendMessage(
                    String.format(
                            "§cAguarde %s para enviar um pedido de teletransporte para esse jogador novamente.",
                            TimeFormatter.formatMinimized(
                                    tpaRequest.getExpireTime() - System.currentTimeMillis()
                            )
                    )
            );
            return;
        }

        JSONText jsonText = new JSONText()
                .text(
                        String.format(
                                "§ePedido enviado para §7%s%s",
                                caribeUser1.hasFaction() ? "[" + caribeUser1.getRolePrefix() + caribeUser1.getFactionTag() + "] " : "",
                                caribeUser1.getPrefix() + caribeUser1.getDisplayName()
                        )
                )
                .next()
                .text("\n")
                .next()
                .text("§eClique ")
                .next()
                .text("§c§lAQUI")
                .execute("/tpa cancelar " + caribeUser1.getName())
                .next()
                .text("§e para cancelar este pedido.")
                .next(),
                jsonText1 = new JSONText()
                        .text(
                                String.format(
                                        "§7%s%s §edeseja teletransportar até você.",
                                        caribeUser.hasFaction() ? "[" + caribeUser.getRolePrefix() + caribeUser.getFactionTag() + "] " : "",
                                        caribeUser.getPrefix() + caribeUser.getDisplayName()
                                )
                        )
                        .next()
                        .text("\n")
                        .next()
                        .text("§eClique ")
                        .next()
                        .text("§a§lAQUI")
                        .execute("/tpa aceitar " + caribeUser.getName())
                        .next()
                        .text("§e para aceitar e ")
                        .next()
                        .text("§c§lAQUI")
                        .execute("/tpa negar " + caribeUser.getName())
                        .next()
                        .text("§e para negar.")
                        .next();

        Player player = (Player) commandSender;

        jsonText.send(player);
        caribeUser1.sendMessage(jsonText1);

        caribeUser.sendTpaRequest(caribeUser1);
    }
}
