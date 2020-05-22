package br.com.twinsflammer.factionsprivateer.commands.player;

import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.spigot.spawn.manager.SpawnManager;
import br.com.twinsflammer.api.spigot.teleport.data.TeleportRequest;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.common.shared.server.manager.ServerManager;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

/**
 * @author SrGutyerrez
 */
public class SpawnCommand extends CustomCommand {
    private final Server server;

    public SpawnCommand() {
        super(
                "spawn",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );

        this.server = ServerManager.getServer(SpigotAPI.getRootServerId());
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] strings) {
        Location location = SpawnManager.DEFAULT_SPAWN;

        if (location == null) return;

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());

        TeleportRequest teleportRequest = new TeleportRequest(
                user.getId(),
                null,
                location,
                this.server.getId(),
                privateerUser.getTeleportTime()
        );

        teleportRequest.start();
    }
}
