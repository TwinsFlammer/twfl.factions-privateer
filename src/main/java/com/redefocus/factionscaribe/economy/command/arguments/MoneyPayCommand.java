package com.redefocus.factionscaribe.economy.command.arguments;

import com.redefocus.api.spigot.commands.CustomArgumentCommand;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.common.shared.util.Helper;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.economy.command.MoneyCommand;
import com.redefocus.factionscaribe.economy.manager.EconomyManager;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class MoneyPayCommand<C extends MoneyCommand> extends CustomArgumentCommand<C> {
    public MoneyPayCommand() {
        super(
                0,
                "pay"
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

            CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());
            CaribeUser caribeUser1 = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user1.getId());

            if (caribeUser1 == null) {
                commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
                return;
            }

            Double money = Helper.isDouble(args[1]) ? Double.parseDouble(args[1]) : null;

            if (money == null || money.isNaN() || money <= 0) {
                commandSender.sendMessage("§cVocê inseriu um valor inválido.");
                return;
            }

            if (caribeUser.getMoney() < money) {
                commandSender.sendMessage("§cVocê não possui saldo suficiente.");
                return;
            }

            caribeUser.withdraw(money);
            caribeUser1.deposit(money);

            commandSender.sendMessage(
                    String.format(
                            "§eO saldo de %s §efoi atualizado para %s.",
                            caribeUser1.getPrefix() + caribeUser1.getDisplayName(),
                            EconomyManager.format(
                                    caribeUser1.getMoney()
                            )
                    )
            );
        } else {
            commandSender.sendMessage("§cUtilize /money pay <usuário> <quantia>.");
        }
    }
}
