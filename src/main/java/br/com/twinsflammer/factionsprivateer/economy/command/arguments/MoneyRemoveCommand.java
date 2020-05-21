package br.com.twinsflammer.factionsprivateer.economy.command.arguments;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.api.spigot.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.factionsprivateer.economy.command.MoneyCommand;
import br.com.twinsflammer.factionsprivateer.economy.event.MoneyChangeEvent;
import br.com.twinsflammer.factionsprivateer.economy.manager.EconomyManager;
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

            PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user1.getId());

            if (privateerUser == null) {
                commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
                return;
            }

            Double money = Helper.isDouble(args[1]) ? Double.parseDouble(args[1]) : null;

            if (money == null || money.isNaN() || money <= 0) {
                commandSender.sendMessage("§cVocê inseriu um valor inválido.");
                return;
            }

            if (privateerUser.getMoney() < money) {
                commandSender.sendMessage("§cEste usuário não possui saldo suficiente.");
                return;
            }

            MoneyChangeEvent moneyChangeEvent = new MoneyChangeEvent(
                    privateerUser,
                    privateerUser.getMoney(),
                    privateerUser.getMoney() - money
            );

            moneyChangeEvent.run();

            if (moneyChangeEvent.isCancelled()) return;

            privateerUser.withdraw(money);

            commandSender.sendMessage(
                    String.format(
                            "§eO saldo de %s §efoi atualizado para %s.",
                            privateerUser.getPrefix() + privateerUser.getDisplayName(),
                            EconomyManager.format(
                                    privateerUser.getMoney()
                            )
                    )
            );
        } else {
            commandSender.sendMessage("§cUtilize /money remove <usuário> <quantia>.");
        }
    }
}
