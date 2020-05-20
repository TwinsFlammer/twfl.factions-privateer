package br.com.twinsflammer.factionscaribe.commands.staff.clear.command;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.factionscaribe.commands.staff.clear.channel.ClearChannel;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONObject;

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
                this.clear(user);

                commandSender.sendMessage("§aVocê teve seu inventário totalmente limpo!");
                return;
            }
            case 1: {
                String targetName = args[0];

                CaribeUser targetUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(targetName);

                if (targetUser == null) {
                    commandSender.sendMessage("§cEste usuário não existe.");
                    return;
                }

                if (!targetUser.isOnline()) {
                    commandSender.sendMessage("§cEste usuário não está online.");
                    return;
                }

                if (!targetUser.isLogged()) {
                    commandSender.sendMessage("§cVocê não pode interagir com usuários não autenticados.");
                    return;
                }

                this.clear(targetUser);

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

    void clear(User user) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("user_id", user.getId());

        ClearChannel clearChannel = new ClearChannel();

        clearChannel.sendMessage(jsonObject.toString());
    }
}
