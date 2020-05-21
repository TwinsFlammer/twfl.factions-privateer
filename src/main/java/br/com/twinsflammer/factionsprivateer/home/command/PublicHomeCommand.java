package br.com.twinsflammer.factionsprivateer.home.command;

import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.home.dao.HomeDao;
import br.com.twinsflammer.factionsprivateer.home.data.Home;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author oNospher
 **/
public class PublicHomeCommand extends CustomCommand {
    public PublicHomeCommand() {
        super(
                "publica",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if(args.length != 1) {
            commandSender.sendMessage("§cUtilize /publica <home>");
            return;
        }

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());
        String name = args[0];

        if(!privateerUser.hasHome(name)) {
            commandSender.sendMessage("§cEsta home não existe.");
            return;
        }

        Home home = privateerUser.getHomeExact(name);

        if(home.isPublic()) {
            commandSender.sendMessage("§cEsta home já é pública.");
            return;
        }

        HomeDao<Home> homeDao = new HomeDao<>();

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("state", Home.State.PUBLIC.toString());

        homeDao.update(hashMap, "id", home.getId());

        home.setState(Home.State.PUBLIC);

        commandSender.sendMessage(
                String.format(
                       "§aPública \"%s\" adicionada com sucesso.",
                        home.getName()
                )
        );
    }
}
