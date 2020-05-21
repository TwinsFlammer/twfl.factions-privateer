package br.com.twinsflammer.factionsprivateer.economy.command;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.economy.command.arguments.*;
import br.com.twinsflammer.factionsprivateer.economy.manager.EconomyManager;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class MoneyCommand extends CustomCommand {
    public MoneyCommand() {
        super(
                "money",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT,
                new String[]{
                        "coins"
                }
        );

        this.addArgument(
                new MoneyTopCommand(),
                new MoneySetCommand(),
                new MoneyRemoveCommand(),
                new MoneyPayCommand(),
                new MoneyGiveCommand()
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(this.showUserMoney(user, true));
            return;
        } else if (args.length == 1) {
            User user1 = UserManager.getUser(args[0]);

            commandSender.sendMessage(this.showUserMoney(user1, false));
            return;
        } else {
            commandSender.sendMessage("§cUtilize /money <usuário>.");
            return;
        }
    }

    String showUserMoney(User user, Boolean myWallet) {
        if (user == null) {
            return "§cEste usuário não existe.";
        }

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());

        if (privateerUser == null) {
            return "§cEste usuário nunca entrou neste servidor.";
        }

        return String.format(
                "§a%s %s§a: §f%s",
                myWallet ? "Sua carteira" : "Carteira de",
                privateerUser.getPrefix() + privateerUser.getDisplayName(),
                EconomyManager.format(
                        privateerUser.getMoney()
                )
        );
    }

}
