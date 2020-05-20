package br.com.twinsflammer.factionscaribe.commands.staff;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SrGutyerrez
 */
public class VanishCommand extends CustomCommand {
    public VanishCommand() {
        super(
                "v",
                CommandRestriction.IN_GAME,
                GroupNames.MODERATOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());

        caribeUser.setInvisible(!caribeUser.isInvisible());

        commandSender.sendMessage("§aO modo invisível foi " + (caribeUser.isInvisible() ? "ativado." : "desativado."));

        Player player = (Player) commandSender;

        Bukkit.getOnlinePlayers().forEach(player1 -> {
            CaribeUser caribeUser1 = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player1.getUniqueId());

            if (caribeUser.isInvisible() && !caribeUser1.isStaff())
                player1.hidePlayer(player);
            else player1.showPlayer(player);
        });
    }
}
