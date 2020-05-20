package br.com.twinsflammer.factionscaribe.spawner.command;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

/**
 * @author SrGutyerrez
 */
public class SpawnerCommand extends CustomCommand {
    public SpawnerCommand() {
        super(
                "spawner",
                CommandRestriction.ALL,
                GroupNames.MASTER
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length != 4 || !args[0].equalsIgnoreCase("give")) {
            commandSender.sendMessage("§cUtilize /spawner give <usuário> <item> <quantia>.");
            return;
        }

        String targetName = args[1];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            commandSender.sendMessage("§cEste usuário não existe.");
            return;
        }

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(targetName);

        if (caribeUser == null) {
            commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
            return;
        }

        if (!caribeUser.isOnlineHere()) {
            commandSender.sendMessage("§cEste usuário não está online.");
            return;
        }

        try {
            EntityType entityType = EntityType.valueOf(args[2]);

            Integer amount = Helper.isInteger(args[3]) ? Integer.parseInt(args[3]) : null;

            if (amount == null || amount <= 0) {
                commandSender.sendMessage("§cVocê inseriu uma quantia inválida.");
                return;
            }


        } catch (NullPointerException exception) {
            commandSender.sendMessage("§c");
        }
    }
}
