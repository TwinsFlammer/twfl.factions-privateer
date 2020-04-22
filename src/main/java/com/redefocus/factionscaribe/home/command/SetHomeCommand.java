package com.redefocus.factionscaribe.home.command;

import com.google.common.collect.Maps;
import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.api.spigot.util.serialize.LocationSerialize;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.home.dao.HomeDao;
import com.redefocus.factionscaribe.home.data.Home;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author oNospher
 **/
public class SetHomeCommand extends CustomCommand {
    private final Integer MAX_HOME_NAME_LENGHT = 32;

    public SetHomeCommand() {
        super(
                "sethome",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if(args.length != 1) {
            commandSender.sendMessage("§cUtilize /sethome <home>");
            return;
        }
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());
        String name = args[0];

        if(name.length() > this.MAX_HOME_NAME_LENGHT) {
            commandSender.sendMessage(
                    String.format(
                            "§cO nome da home tem que ter no máximo %d.",
                            this.MAX_HOME_NAME_LENGHT
                    )
            );
            return;
        }

        HomeDao<Home> homeDao = new HomeDao<>();

        Home home = caribeUser.getHome(name);

        home.setLocation(caribeUser.getLocation());
        home.setServerId(caribeUser.getServerId());

        if (caribeUser.hasHome(name)) {
            HashMap<String, Object> keys = Maps.newHashMap();

            keys.put("location", LocationSerialize.toString(
                    caribeUser.getLocation()
            ));
            keys.put("server_id", home.getServerId());

            homeDao.update(
                    keys,
                    "id",
                    home.getId()
            );
        } else {
            home = homeDao.insert(home);

            caribeUser.addHome(home);
        }

        commandSender.sendMessage(
                String.format(
                        "§aPronto! Sua home foi definida com sucesso. Para voltar para esta localização utilize \"§f/home %s§a\".",
                        name
                )
        );
    }
}
