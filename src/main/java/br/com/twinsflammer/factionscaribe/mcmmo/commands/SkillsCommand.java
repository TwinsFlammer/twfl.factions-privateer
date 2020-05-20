package br.com.twinsflammer.factionscaribe.mcmmo.commands;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkillsCommand extends CustomCommand {
    public SkillsCommand() {
        super(
                "skills",
                CommandRestriction.IN_GAME,
                GroupNames.DEFAULT,
                new String[]{
                        "inspect"
                }
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());
        Player player = (Player) commandSender;

        if (args.length == 0) {
            player.openInventory(caribeUser.getSkillsInventory());
        } else {
            User user1 = UserManager.getUser(args[0]);

            if (user1 == null) {
                commandSender.sendMessage("§cEste usuário não existe.");
                return;
            }

            CaribeUser caribeUser1 = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user1.getId());

            if (caribeUser1 == null) {
                commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
                return;
            }

            player.openInventory(caribeUser1.getSkillsInventory());
        }
    }
}
