package com.redefocus.factionscaribe.home.command;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.api.spigot.teleport.data.TeleportRequest;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.home.component.HomeComponent;
import com.redefocus.factionscaribe.home.data.Home;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

import java.util.List;

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
        switch (args.length) {
            case 1: {
                if (args[0].contains(":")) {
                    String[] arguments = args[0].split(":");

                    String targetName = arguments[0];

                    CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(targetName);

                    if (caribeUser == null) {
                        commandSender.sendMessage("§cEste usuário não existe.");
                        return;
                    }

                    if (arguments.length == 2) {
                        String homeName = arguments[1];

                        Home home = caribeUser.getHomeExact(homeName);

                        if (home == null) {
                            commandSender.sendMessage("§cEsta home não existe.");
                            return;
                        }

                        if (home.isPrivate() && !caribeUser.getId().equals(user.getId())) {
                            commandSender.sendMessage("§cEsta não é uma home pública.");
                            return;
                        }

                        CaribeUser caribeUser1 = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getUniqueId());

                        if (!caribeUser1.canTeleport(home)) {
                            commandSender.sendMessage("§cVocê não pode teleportar-se para este local.");
                            return;
                        }

                        TeleportRequest teleportRequest = new TeleportRequest(
                                user.getId(),
                                null,
                                home.getLocation(),
                                home.getServerId(),
                                caribeUser.getTeleportTime()
                        );

                        teleportRequest.start();
                        return;
                    } else {
                        this.showHomes(commandSender, caribeUser);
                        return;
                    }
                } else {
                    CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getUniqueId());

                    String homeName = args[0];

                    Home home = caribeUser.getHomeExact(homeName);

                    if (home == null) {
                        commandSender.sendMessage("§cEsta home não existe.");
                        return;
                    }

                    if (!caribeUser.canTeleport(home)) {
                        commandSender.sendMessage("§cVocê não pode teleportar-se para este local.");
                        return;
                    }

                    TeleportRequest teleportRequest = new TeleportRequest(
                            user.getId(),
                            null,
                            home.getLocation(),
                            home.getServerId(),
                            caribeUser.getTeleportTime()
                    );

                    teleportRequest.start();
                    return;
                }
            }
            default: {
                CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getUniqueId());

                this.showHomes(commandSender, caribeUser);
                return;
            }
        }
    }

    void showHomes(CommandSender commandSender, CaribeUser targetUser) {
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(commandSender.getName());

        Boolean owner = caribeUser.getId().equals(targetUser.getId());

        List<Home> homes = caribeUser.getHomes();

        HomeComponent publics = new HomeComponent(homes, Home.State.PUBLIC) {
            @Override
            public void send(CaribeUser caribeUser) {
                super.send(caribeUser);
            }
        }, privates = new HomeComponent(homes, Home.State.PRIVATE) {
            @Override
            public void send(CaribeUser caribeUser) {
                super.send(caribeUser);
            }
        };

        commandSender.sendMessage(
                new String[]{
                        "§cUtilize /home <usuário>:<nome>.",
                        ""
                }
        );
        publics.send(caribeUser);
        if (owner || targetUser.hasGroup(GroupNames.COORDINATOR))
            privates.send(caribeUser);
    }
}
