package com.redefocus.factionscaribe.commands.player;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.api.spigot.spawn.manager.SpawnManager;
import com.redefocus.api.spigot.teleport.data.TeleportRequest;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.server.data.Server;
import com.redefocus.common.shared.server.manager.ServerManager;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
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

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());

        TeleportRequest teleportRequest = new TeleportRequest(
                user.getId(),
                null,
                location,
                this.server.getId(),
                caribeUser.getTeleportTime()
        );

        teleportRequest.start();
    }
}
