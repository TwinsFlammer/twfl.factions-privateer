package com.redefocus.factionscaribe.economy.command.arguments;

import com.redefocus.api.spigot.commands.CustomArgumentCommand;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.common.shared.util.Helper;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.economy.command.MoneyCommand;
import com.redefocus.factionscaribe.economy.event.MoneyChangeEvent;
import com.redefocus.factionscaribe.economy.manager.EconomyManager;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class MoneyRemoveCommand<C extends MoneyCommand> extends CustomArgumentCommand<C> {
    public MoneyRemoveCommand() {
        super(
                0,
                "remove"
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length == 2) {
            User user1 = UserManager.getUser(args[0]);

            if (user1 == null) {
                commandSender.sendMessage("§cEste usuário não existe.");
                return;
            }

            CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user1.getId());

            if (caribeUser == null) {
                commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
                return;
            }

            Double money = Helper.isDouble(args[1]) ? Double.parseDouble(args[1]) : null;

            if (money == null || money.isNaN() || money <= 0) {
                commandSender.sendMessage("§cVocê inseriu um valor inválido.");
                return;
            }

            if (caribeUser.getMoney() < money) {
                commandSender.sendMessage("§cEste usuário não possui saldo suficiente.");
                return;
            }

            MoneyChangeEvent moneyChangeEvent = new MoneyChangeEvent(
                    caribeUser,
                    caribeUser.getMoney(),
                    caribeUser.getMoney() - money
            );

            moneyChangeEvent.run();

            if (moneyChangeEvent.isCancelled()) return;

            caribeUser.withdraw(money);

            commandSender.sendMessage(
                    String.format(
                            "§eO saldo de %s §efoi atualizado para %s.",
                            caribeUser.getPrefix() + caribeUser.getDisplayName(),
                            EconomyManager.format(
                                    caribeUser.getMoney()
                            )
                    )
            );
        } else {
            commandSender.sendMessage("§cUtilize /money remove <usuário> <quantia>.");
        }
    }
}
