package br.com.twinsflammer.factionsprivateer.economy.command.arguments;

import br.com.twinsflammer.api.spigot.commands.CustomArgumentCommand;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.economy.command.MoneyCommand;
import br.com.twinsflammer.factionsprivateer.economy.event.MoneyChangeEvent;
import br.com.twinsflammer.factionsprivateer.economy.manager.EconomyManager;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
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

            PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());
            PrivateerUser privateerUser1 = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user1.getId());

            if (privateerUser1 == null) {
                commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
                return;
            }

            Double money = Helper.isDouble(args[1]) ? Double.parseDouble(args[1]) : null;

            if (money == null || money.isNaN() || money <= 0) {
                commandSender.sendMessage("§cVocê inseriu um valor inválido.");
                return;
            }

            if (privateerUser.getMoney() < money) {
                commandSender.sendMessage("§cVocê não possui saldo suficiente.");
                return;
            }

            if (privateerUser.isSimilar(privateerUser1)) {
                commandSender.sendMessage("§cVocê não pode enviar dinheiro para si mesmo.");
                return;
            }

            MoneyChangeEvent moneyChangeEvent1 = new MoneyChangeEvent(
                    privateerUser,
                    privateerUser.getMoney(),
                    privateerUser.getMoney() - money
            );

            moneyChangeEvent1.run();

            if (moneyChangeEvent1.isCancelled()) return;

            MoneyChangeEvent moneyChangeEvent2 = new MoneyChangeEvent(
                    privateerUser1,
                    privateerUser1.getMoney(),
                    privateerUser1.getMoney() + money
            );

            moneyChangeEvent2.run();

            if (moneyChangeEvent2.isCancelled()) return;

            privateerUser.withdraw(money);
            privateerUser1.deposit(money);

            commandSender.sendMessage(
                    String.format(
                            "§eO saldo de %s §efoi atualizado para %s.",
                            privateerUser1.getPrefix() + privateerUser1.getDisplayName(),
                            EconomyManager.format(
                                    privateerUser1.getMoney()
                            )
                    )
            );
        } else {
            commandSender.sendMessage("§cUtilize /money pay <usuário> <quantia>.");
        }
    }
}
