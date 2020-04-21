package com.redefocus.factionscaribe.commands.staff;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author SrGutyerrez
 */
public class ClearCommand extends CustomCommand {
    public ClearCommand() {
        super(
                "clear",
                CommandRestriction.IN_GAME,
                GroupNames.COORDINATOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        switch (args.length) {
            case 0: {
                Player player = (Player) commandSender;

                this.clear(player);

                commandSender.sendMessage("§aVocê teve seu inventário totalmente limpo!");
                return;
            }
            case 1: {
                String targetName = args[0];

                Player targetPlayer = Bukkit.getPlayerExact(targetName);

                CaribeUser targetUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(targetName);

                if (targetUser == null) {
                    commandSender.sendMessage("§cEste usuário não existe.");
                    return;
                }

                if (!targetUser.isOnline()) {
                    commandSender.sendMessage("§cEste usuário não está online.");
                    return;
                }

                this.clear(targetPlayer);

                commandSender.sendMessage(
                        String.format(
                                "§aVocê limpou o inventário de %s §acom sucesso.",
                                targetUser.getPrefix() + targetUser.getDisplayName()
                        )
                );
                return;
            }
        }
    }

    void clear(Player player) {
        PlayerInventory playerInventory = player.getInventory();

        playerInventory.clear();

        playerInventory.setArmorContents(null);
    }
}
