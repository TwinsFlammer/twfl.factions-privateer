package com.redefocus.factionscaribe.economy.command.arguments;

import com.redefocus.api.spigot.commands.CustomArgumentCommand;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.economy.command.MoneyCommand;
import com.redefocus.factionscaribe.economy.manager.EconomyManager;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author SrGutyerrez
 */
public class MoneyTopCommand<C extends MoneyCommand> extends CustomArgumentCommand<C> {
    public MoneyTopCommand() {
        super(
                0,
                "top"
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] strings) {
        commandSender.sendMessage(
                new String[]{
                        "",
                        "§aRank com os usuários mais ricos §7(atualizado a cada 5 minutos)",
                        ""
                }
        );

        List<CaribeUser> TOP_LIST = EconomyManager.getTopList();

        IntStream.range(0, TOP_LIST.size())
                .forEach(index -> {
                    CaribeUser caribeUser = TOP_LIST.get(index);

                    commandSender.sendMessage(
                            String.format(
                                    " - %dº %s §7(%s coins)",
                                    index + 1,
                                    caribeUser.getPrefix() + caribeUser.getDisplayName(),
                                    EconomyManager.format(caribeUser.getMoney())
                            )
                    );
                });

        commandSender.sendMessage("");
    }
}
