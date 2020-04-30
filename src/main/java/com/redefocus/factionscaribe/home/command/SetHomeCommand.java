package com.redefocus.factionscaribe.home.command;

import com.google.common.collect.Maps;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.api.spigot.util.serialize.LocationSerialize;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.home.dao.HomeDao;
import com.redefocus.factionscaribe.home.data.Home;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import com.redefocus.factionscaribe.util.FactionUtil;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author oNospher
 **/
public class SetHomeCommand extends CustomCommand {
    private final Integer MAX_HOME_NAME_LENGHT = 32, MIN_HOME_NAME_LENGTH = 3;
    private final String[] WORLDS_PERMITTED = {"world"};

    public SetHomeCommand() {
        super(
                "sethome",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage("§cUtilize /sethome <home>");
            return;
        }

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());
        String name = args[0];

        Integer serverId = caribeUser.getServerId();

        if (!this.isWorldAllowed(caribeUser.getWorld()) || !FactionUtil.isAllowed() || !SpigotAPI.getRootServerId().equals(serverId)) {
            commandSender.sendMessage(
                    "§cVocê não pode definir home nesse lugar."
            );
            return;
        }

        if (name.length() < this.MIN_HOME_NAME_LENGTH || name.length() > this.MAX_HOME_NAME_LENGHT) {
            commandSender.sendMessage(
                    String.format(
                            "§cO nome da home deve ter entre %d a %d caracteres .",
                            this.MIN_HOME_NAME_LENGTH,
                            this.MAX_HOME_NAME_LENGHT
                    )
            );
            return;
        }

        HomeDao<Home> homeDao = new HomeDao<>();

        Home home = caribeUser.getHome(name);

        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(caribeUser.getLocation()));

        home.setLocation(caribeUser.getLocation());
        home.setServerId(caribeUser.getServerId());
        home.setFactionId(factionAt.getId());

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
                        "§aHome '%s' criada com sucesso.",
                        name
                )
        );
    }

    Boolean isWorldAllowed(World world) {
        return Arrays.asList(this.WORLDS_PERMITTED).contains(world.getName());
    }
}
