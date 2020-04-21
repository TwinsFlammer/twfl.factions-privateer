package com.redefocus.factionscaribe.commands.staff;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author SrGutyerrez
 */
public class GameModeCommand extends CustomCommand {
    public GameModeCommand() {
        super(
                "gamemode",
                CommandRestriction.IN_GAME,
                GroupNames.DIRECTOR,
                new String[] {
                        "gm"
                }
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        switch (args.length){
            case 1: {
                Player player = (Player) commandSender;

                GameMode gameMode = this.matchGameMode(args[0]);

                player.setGameMode(gameMode);

                commandSender.sendMessage("§aSeu modo de jogo foi atualizado para " + gameMode.name().toLowerCase() + ".");
                return;
            }
            case 2: {
                String targetName = args[0];
                Player targetPlayer = Bukkit.getPlayerExact(targetName);

                if (targetPlayer == null){
                    commandSender.sendMessage("§cEste usuário não está online.");
                    return;
                }

                GameMode gameMode = this.matchGameMode(args[1]);
                targetPlayer.setGameMode(gameMode);

                commandSender.sendMessage("§aO modo de jogo de " + targetPlayer.getName() + " foi atualizado para " + gameMode.name().toLowerCase() + ".");
                return;
            }
            default: {
                commandSender.sendMessage("§cUtilize /gamemode <modo>.");
                return;
            }
        }
    }

    private GameMode matchGameMode(String value){
        value = value.toLowerCase();
        if (value.startsWith("0") || value.startsWith("su")){
            return GameMode.SURVIVAL;
        } else if (value.startsWith("1") || value.startsWith("c")){
            return GameMode.CREATIVE;
        } else if (value.startsWith("2") || value.startsWith("a")){
            return GameMode.ADVENTURE;
        } else if (value.startsWith("3") || value.startsWith("sp") || value.startsWith("e")){
            return GameMode.SPECTATOR;
        } else {
            return GameMode.SURVIVAL;
        }
    }
}
