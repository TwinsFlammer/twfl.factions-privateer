package br.com.twinsflammer.factionsprivateer.mcmmo.commands.experience;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.PlayerProfile;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.XPGainReason;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Permissions;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddxpCommand extends ExperienceCommand {

    @Override
    protected boolean permissionsCheckSelf(CommandSender sender) {
        return Permissions.addxp(sender);
    }

    @Override
    protected boolean permissionsCheckOthers(CommandSender sender) {
        return Permissions.addxpOthers(sender);
    }

    @Override
    protected void handleCommand(Player player, PlayerProfile profile, SkillType skill, int value) {
        if (player != null) {
            UserManager.getPlayer(player).applyXpGain(skill, value, XPGainReason.COMMAND);
        } else {
            profile.addXp(skill, value);
            profile.scheduleAsyncSave();
        }
    }

    @Override
    protected void handlePlayerMessageAll(Player player, int value) {
        player.sendMessage(LocaleLoader.getString("Commands.addxp.AwardAll", value));
    }

    @Override
    protected void handlePlayerMessageSkill(Player player, int value, SkillType skill) {
        player.sendMessage(LocaleLoader.getString("Commands.addxp.AwardSkill", value, skill.getName()));
    }
}
