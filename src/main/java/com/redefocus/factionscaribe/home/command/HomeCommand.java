package com.redefocus.factionscaribe.home.command;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.chat.component.ChatComponent;
import com.redefocus.factionscaribe.chat.enums.Channel;
import com.redefocus.factionscaribe.home.component.HomeComponent;
import com.redefocus.factionscaribe.home.data.Home;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

/**
 * @author oNospher
 **/
public class HomeCommand extends CustomCommand {

    public HomeCommand() {
        super(
                "home",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT,
                new String[]{
                        "homes"
                }
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());
        switch (args.length) {
            case 1: {
                String argument = args[0];
                if(argument.contains(":")) {
                    String owner = argument.split(":")[0];
                    CaribeUser caribeTarget = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(owner);
                    if(caribeTarget == null) {
                        commandSender.sendMessage("§cEste usuário não existe.");
                        return;
                    }
                    try {
                        String name = argument.split(":")[1];
                        Home home = caribeTarget.getHomeExact(name);

                    } catch (Exception exception) {
                        HomeComponent homeComponent = new HomeComponent(caribeTarget) {
                            @Override
                            public void send(CaribeUser caribeUser) {
                                super.send(caribeUser);
                            }
                        };
                        homeComponent.send(caribeUser);
                    }

                } else {

                }
            }
            default: {
                HomeComponent homeComponent = new HomeComponent(caribeUser) {
                    @Override
                    public void send(CaribeUser caribeUser) {
                        super.send(caribeUser);
                    }
                };
                homeComponent.send(caribeUser);
            }
        }
    }
}
