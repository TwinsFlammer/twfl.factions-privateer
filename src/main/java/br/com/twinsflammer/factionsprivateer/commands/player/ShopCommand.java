package br.com.twinsflammer.factionsprivateer.commands.player;

import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.spigot.teleport.data.TeleportRequest;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SrGutyerrez
 */
public class ShopCommand extends CustomCommand {
    private final Location SHOP_LOCATION;

    public ShopCommand() {
        super(
                "shop",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );

        World world = Bukkit.getWorld("world");

        this.SHOP_LOCATION = new Location(
                world,
                0,
                4,
                0
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] strings) {
        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());

        TeleportRequest teleportRequest = new TeleportRequest(
                user.getId(),
                null,
                this.SHOP_LOCATION,
                SpigotAPI.getRootServerId(),
                privateerUser.getTeleportTime()
        );

        teleportRequest.start();
    }
}
