package com.redefocus.factionscaribe.home.command;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.api.spigot.util.serialize.LocationSerialize;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.home.dao.HomeDao;
import com.redefocus.factionscaribe.home.data.Home;
import com.redefocus.factionscaribe.home.enums.HomeState;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author oNospher
 **/
public class SetHomeCommand extends CustomCommand {

    public SetHomeCommand() {
        super(
                "sethome",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );
    }

    @Override
    public void onCommand(CommandSender sender, User user, String[] args) {
        if(args.length != 1) {
            sender.sendMessage("§cUtilize /sethome <home>");
            return;
        }
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());
        String name = args[0];

        if(name.length() > 32) {
            sender.sendMessage(
                    String.format(
                            "§cO nome da home tem que ter menos de %s caracteres.",
                            32
                    )
            );
            return;
        }

        HomeDao<Home> homeDao = new HomeDao<>();

        if(caribeUser.hasHome(name)) {
            Home home = caribeUser.getHome(name);

            home.setLocation(caribeUser.getPlayer().getLocation());
            home.setServerId(caribeUser.getServer().getId());

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("location", LocationSerialize.toString(caribeUser.getPlayer().getLocation()));
            hashMap.put("server_id", caribeUser.getServer().getId());
            homeDao.update(hashMap, "id", home.getId());

            sender.sendMessage(
                    String.format(
                            "§aVocê atualizou a home %s com sucesso.",
                            home.getName()
                    )
            );
            return;
        }

        Home home = homeDao.insert(
                new Home(
                        0,
                        caribeUser.getId(),
                        name,
                        caribeUser.getServer().getId(),
                        caribeUser.getPlayer().getLocation(),
                        HomeState.PRIVATE
                )
        );

        caribeUser.getHomes().add(home);

        sender.sendMessage(
                String.format(
                        "§aVocê setou a home %s com sucesso.",
                        32
                )
        );
    }
}
