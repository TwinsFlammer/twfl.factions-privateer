package br.com.twinsflammer.factionscaribe.home.command;

import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.home.dao.HomeDao;
import br.com.twinsflammer.factionscaribe.home.data.Home;
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

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());
        String name = args[0];
        Home home = caribeUser.getHomeExact(name);

        if(home == null) {
            commandSender.sendMessage("§cEssa home não existe.");
            return;
        }

        HomeDao<Home> homeDao = new HomeDao<>();
        homeDao.delete("id", home.getId());

        caribeUser.getHomes().removeIf(home1 -> home1.getId().equals(home.getId()));

        commandSender.sendMessage(
                String.format(
                        "§eA home \"%s\" foi deletada com sucesso.",
                        home.getName()
                )
        );

    }
}
