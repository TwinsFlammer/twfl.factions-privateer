package com.redefocus.factionscaribe.home.command;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
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
public class PrivateHomeCommand extends CustomCommand {
    public PrivateHomeCommand() {
        super(
                "particular",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if(args.length != 1) {
            commandSender.sendMessage("§cUtilize /particular <home>");
            return;
        }

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());
        String name = args[0];

        if(!caribeUser.hasHome(name)) {
            commandSender.sendMessage("§cEsta home não existe.");
            return;
        }

        Home home = caribeUser.getHomeExact(name);

        if(home.isPrivate()) {
            commandSender.sendMessage("§cEsta home já é pública.");
            return;
        }

        HomeDao<Home> homeDao = new HomeDao<>();

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("state", Home.State.PRIVATE.toString());

        homeDao.update(hashMap, "id", home.getId());

        home.setState(Home.State.PRIVATE);

        commandSender.sendMessage(
                String.format(
                        "§aParticular \"%s\" adicionada com sucesso.",
                        home.getName()
                )
        );
    }
}
