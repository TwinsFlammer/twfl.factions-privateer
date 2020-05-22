package br.com.twinsflammer.factionsprivateer.commands.player.tpa.command;

import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.spigot.util.jsontext.data.JSONText;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.util.TimeFormatter;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.commands.player.tpa.command.arguments.TpaAcceptCommand;
import br.com.twinsflammer.factionsprivateer.commands.player.tpa.command.arguments.TpaCancelCommand;
import br.com.twinsflammer.factionsprivateer.commands.player.tpa.command.arguments.TpaDenyCommand;
import br.com.twinsflammer.factionsprivateer.commands.player.tpa.data.TpaRequest;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
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

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());
        PrivateerUser privateerUser1 = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(targetName);

        if (privateerUser1 == null) {
            commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
            return;
        }

        if (privateerUser.isSimilar(privateerUser1)) {
            commandSender.sendMessage("§cVocê não pode pedir para teletransporta-se a si mesmo.");
            return;
        }

        if (!privateerUser1.isOnline() || !SpigotAPI.getSubServersId().contains(privateerUser1.getServerId())) {
            commandSender.sendMessage("§cEste usuário não está online.");
            return;
        }

        TpaRequest tpaRequest = privateerUser.getTeleportRequestsSent()
                .stream()
                .filter(tpaRequest1 -> tpaRequest1.getTargetId().equals(privateerUser1.getId()))
                .filter(tpaRequest1 -> !tpaRequest1.hasExpired())
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

        if (!privateerUser.canSendTpaAgain()) {
            commandSender.sendMessage(
                    String.format(
                            "§cAguarde %s para enviar um pedido de teletransporte novamente.",
                            TimeFormatter.formatMinimized(
                                    privateerUser.getTimeToSendTpaAgain() - System.currentTimeMillis()
                            )
                    )
            );
            return;
        }

        JSONText jsonText = new JSONText()
                .text(
                        String.format(
                                "§ePedido enviado para §7%s%s§e.",
                                privateerUser1.hasFaction() ? "[" + privateerUser1.getRolePrefix() + privateerUser1.getFactionTag() + "] " : "",
                                privateerUser1.getPrefix() + privateerUser1.getDisplayName()
                        )
                )
                .next()
                .text("\n")
                .next()
                .text("§eClique ")
                .next()
                .text("§c§lAQUI")
                .execute("/tpa cancelar " + privateerUser1.getName())
                .next()
                .text("§e para cancelar este pedido.")
                .next(),
                jsonText1 = new JSONText()
                        .text(
                                String.format(
                                        "§7%s%s §edeseja teletransportar até você.",
                                        privateerUser.hasFaction() ? "[" + privateerUser.getRolePrefix() + privateerUser.getFactionTag() + "] " : "",
                                        privateerUser.getPrefix() + privateerUser.getDisplayName()
                                )
                        )
                        .next()
                        .text("\n")
                        .next()
                        .text("§eClique ")
                        .next()
                        .text("§a§lAQUI")
                        .execute("/tpa aceitar " + privateerUser.getName())
                        .next()
                        .text("§e para aceitar e ")
                        .next()
                        .text("§c§lAQUI")
                        .execute("/tpa negar " + privateerUser.getName())
                        .next()
                        .text("§e para negar.")
                        .next();

        Player player = (Player) commandSender;

        jsonText.send(player);
        privateerUser1.sendMessage(jsonText1);

        privateerUser.sendTpaRequest(privateerUser1);
    }
}
