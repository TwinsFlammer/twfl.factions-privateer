package br.com.twinsflammer.factionsprivateer.mcmmo.commands;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
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
        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());
        Player player = (Player) commandSender;

        commandSender.sendMessage("Opa");

        if (args.length == 0) {
            player.openInventory(privateerUser.getSkillsInventory());
        } else {
            User user1 = UserManager.getUser(args[0]);

            if (user1 == null) {
                commandSender.sendMessage("§cEste usuário não existe.");
                return;
            }

            PrivateerUser privateerUser1 = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user1.getId());

            if (privateerUser1 == null) {
                commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
                return;
            }

            player.openInventory(privateerUser1.getSkillsInventory());
        }
    }
}
