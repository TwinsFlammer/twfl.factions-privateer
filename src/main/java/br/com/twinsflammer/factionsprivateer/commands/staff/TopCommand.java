package br.com.twinsflammer.factionsprivateer.commands.staff;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
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
