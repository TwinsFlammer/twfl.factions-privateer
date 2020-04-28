package com.redefocus.factionscaribe.mcmmo.commands.experience;

import com.redefocus.factionscaribe.mcmmo.datatypes.player.PlayerProfile;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.XPGainReason;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.util.EventUtils;
import com.redefocus.factionscaribe.mcmmo.util.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddlevelsCommand extends ExperienceCommand {

    @Override
    protected boolean permissionsCheckSelf(CommandSender sender) {
        return Permissions.addlevels(sender);
    }

    @Override
    protected boolean permissionsCheckOthers(CommandSender sender) {
        return Permissions.addlevelsOthers(sender);
    }

    @Override
    protected void handleCommand(Player player, PlayerProfile profile, SkillType skill, int value) {
        float xpRemoved = profile.getSkillXpLevelRaw(skill);
        profile.addLevels(skill, value);

        if (player == null) {
            profile.scheduleAsyncSave();
            return;
        }

        EventUtils.handleLevelChangeEvent(player, skill, value, xpRemoved, true, XPGainReason.COMMAND);
    }

    @Override
    protected void handlePlayerMessageAll(Player player, int value) {
        player.sendMessage(LocaleLoader.getString("Commands.addlevels.AwardAll.1", value));
    }

    @Override
    protected void handlePlayerMessageSkill(Player player, int value, SkillType skill) {
        player.sendMessage(LocaleLoader.getString("Commands.addlevels.AwardSkill.1", value, skill.getName()));
    }
}
