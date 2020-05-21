package br.com.twinsflammer.factionsprivateer.home.command;

import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import com.google.common.collect.Maps;
import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.spigot.util.serialize.LocationSerialize;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.home.dao.HomeDao;
import br.com.twinsflammer.factionsprivateer.home.data.Home;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author oNospher
 **/
public class SetHomeCommand extends CustomCommand {
    private final Integer MAX_HOME_NAME_LENGHT = 32, MIN_HOME_NAME_LENGTH = 3;
    private final String[] WORLDS_PERMITTED = { "world" };

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

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());
        String name = args[0];

        if (!this.canSetHomeHere(privateerUser)) {
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

        Home home = privateerUser.getHome(name);

        home.setLocation(privateerUser.getLocation());
        home.setServerId(privateerUser.getServerId());
        home.setFactionId(privateerUser.getFactionAtId());

        if (privateerUser.hasHome(name)) {
            HashMap<String, Object> keys = Maps.newHashMap();

            keys.put("location", LocationSerialize.toString(
                    privateerUser.getLocation()
            ));
            keys.put("server_id", home.getServerId());

            homeDao.update(
                    keys,
                    "id",
                    home.getId()
            );
        } else {
            if (privateerUser.getHomes().size() >= privateerUser.getHomeLimit()) {
                commandSender.sendMessage(
                        String.format(
                                "§cVocê atingiu o limite de %d homes definidas.",
                                privateerUser.getHomeLimit()
                        )
                );
                return;
            }

            home = homeDao.insert(home);

            privateerUser.addHome(home);
        }

        commandSender.sendMessage(
                String.format(
                        "§aHome '%s' criada com sucesso.",
                        name
                )
        );
    }

    Boolean canSetHomeHere(PrivateerUser privateerUser) {
        Integer serverId = privateerUser.getServerId();

        World world = privateerUser.getWorld();

        if (!SpigotAPI.getRootServerId().equals(serverId)
                || !Arrays.asList(this.WORLDS_PERMITTED).contains(world.getName()))
            return false;

        Location location = privateerUser.getLocation();

        return privateerUser.canTeleport(location);
    }
}
