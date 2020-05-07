package com.redefocus.factionscaribe.commands.player;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
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
        Player player = (Player) commandSender;

        player.teleport(this.SHOP_LOCATION);
    }
}
