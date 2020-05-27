package br.com.twinsflammer.factionsprivateer.home.command;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.spigot.teleport.data.TeleportRequest;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.home.component.HomeComponent;
import br.com.twinsflammer.factionsprivateer.home.data.Home;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
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

                    PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(targetName);

                    if (privateerUser == null) {
                        commandSender.sendMessage("§cEste usuário não existe.");
                        return;
                    }

                    if (arguments.length == 2) {
                        String homeName = arguments[1];

                        Home home = privateerUser.getHomeExact(homeName);

                        if (home == null) {
                            commandSender.sendMessage("§cEsta home não existe.");
                            return;
                        }

                        if (home.isPrivate() && !privateerUser.getId().equals(user.getId())) {
                            commandSender.sendMessage("§cEsta não é uma home pública.");
                            return;
                        }

                        PrivateerUser privateerUser1 = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getUniqueId());

                        if (!privateerUser1.canTeleport(home)) {
                            commandSender.sendMessage("§cVocê não pode teleportar-se para este local.");
                            return;
                        }

                        TeleportRequest teleportRequest = new TeleportRequest(
                                user.getId(),
                                null,
                                home.getLocation(),
                                home.getServerId(),
                                privateerUser.getTeleportTime()
                        );

                        teleportRequest.start();
                        return;
                    } else {
                        this.showHomes(commandSender, privateerUser);
                        return;
                    }
                } else {
                    PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getUniqueId());

                    String homeName = args[0];

                    Home home = privateerUser.getHomeExact(homeName);

                    if (home == null) {
                        commandSender.sendMessage("§cEsta home não existe.");
                        return;
                    }

                    if (!privateerUser.canTeleport(home)) {
                        commandSender.sendMessage("§cVocê não pode teleportar-se para este local.");
                        return;
                    }

                    TeleportRequest teleportRequest = new TeleportRequest(
                            user.getId(),
                            null,
                            home.getLocation(),
                            home.getServerId(),
                            privateerUser.getTeleportTime()
                    );

                    teleportRequest.start();
                    return;
                }
            }
            default: {
                PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getUniqueId());

                this.showHomes(commandSender, privateerUser);
                return;
            }
        }
    }

    void showHomes(CommandSender commandSender, PrivateerUser targetUser) {
        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(commandSender.getName());

        Boolean owner = privateerUser.getId().equals(targetUser.getId());

        List<Home> homes = targetUser.getHomes();

        HomeComponent publics = new HomeComponent(homes, Home.State.PUBLIC) {
            @Override
            public void send(PrivateerUser caribeUser) {
                super.send(caribeUser);
            }
        }, privates = new HomeComponent(homes, Home.State.PRIVATE) {
            @Override
            public void send(PrivateerUser caribeUser) {
                super.send(caribeUser);
            }
        };

        commandSender.sendMessage(
                new String[]{
                        "§cUtilize /home <usuário>:<nome>.",
                        ""
                }
        );
        publics.send(privateerUser);
        if (owner || targetUser.hasGroup(GroupNames.COORDINATOR))
            privates.send(privateerUser);
    }
}
