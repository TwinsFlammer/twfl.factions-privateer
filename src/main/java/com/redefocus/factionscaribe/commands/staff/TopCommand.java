package com.redefocus.factionscaribe.commands.staff;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SrGutyerrez
 */
public class TopCommand extends CustomCommand {
    public TopCommand() {
        super(
                "top",
                CommandRestriction.IN_GAME,
                GroupNames.ADMINISTRATOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        Player player = (Player) commandSender;

        Location location = player.getLocation().clone();
        World world = location.getWorld();

        location.setY(
                world.getHighestBlockYAt(location)
        );

        player.teleport(location);

        commandSender.sendMessage("§eTeleportado para o topo.");
    }
}
