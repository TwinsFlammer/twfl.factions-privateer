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

/**
 * @author oNospher
 **/
public class DelHomeCommand extends CustomCommand {

    public DelHomeCommand() {
        super(
                "delhome",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if(args.length != 1) {
            commandSender.sendMessage("§cUtilize /delhome <home>");
            return;
        }

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());
        String name = args[0];
        Home home = privateerUser.getHomeExact(name);

        if(home == null) {
            commandSender.sendMessage("§cEssa home não existe.");
            return;
        }

        HomeDao<Home> homeDao = new HomeDao<>();
        homeDao.delete("id", home.getId());

        privateerUser.getHomes().removeIf(home1 -> home1.getId().equals(home.getId()));

        commandSender.sendMessage(
                String.format(
                        "§eA home \"%s\" foi deletada com sucesso.",
                        home.getName()
                )
        );

    }
}
