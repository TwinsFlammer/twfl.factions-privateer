package com.redefocus.factionscaribe.mcmmo.commands;

//import br.com.titanwar.api.API;
//import br.com.titanwar.api.commands.CustomCommand;
//import br.com.titanwar.api.user.SpigotUser;
//import br.com.titanwar.common.shared.group.EnumGroup;
//import br.com.titanwar.common.shared.users.UserProfileDatabase;
//import br.com.titanwar.common.shared.users.modules.UserProfile;
//import br.com.titanwar.common.shared.utils.CommandRestriction;
//import br.com.titanwar.common.shared.utils.MessageUtils;
//import br.com.titanwar.server.user.ServerUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSkills /*extends CustomCommand*/ {

    /*
    public CommandSkills() {
        super("skills", EnumGroup.DEFAULT, CommandRestriction.INGAME, "habilidades", "stats", "inspect");
    }

    @Override
    public void onCommand(CommandSender sender, SpigotUser spigotUser, String[] args) {

        ServerUser user = (ServerUser) spigotUser;
        Player player = (Player) sender;

        if (args.length == 0) {
            player.openInventory(user.getSkillsInventory());
        } else {

            if (args[0].equalsIgnoreCase("top")) {
                player.openInventory(user.getTopSkillsInventory());
                return;
            }

            UserProfile targetProfile = UserProfileDatabase.getUserProfileNetwork(args[0]);

            if (targetProfile == null) {
                player.sendMessage(MessageUtils.translateColorCodes("&cUsuario não encontrado."));
                return;
            }

            ServerUser targetUser = (ServerUser) API.getInstance().getUserFactory().getUser(targetProfile.getId());

            if (targetUser == null) {
                targetUser = new ServerUser(targetProfile);
            }

            player.openInventory(targetUser.getSkillsInventory());

        }
    }
    */
}
