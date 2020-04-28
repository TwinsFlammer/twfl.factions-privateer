package com.redefocus.factionscaribe.mcmmo.commands.hardcore;

import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.util.Permissions;
import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.command.CommandSender;

public class HardcoreCommand extends HardcoreModeCommand {

    @Override
    protected boolean checkTogglePermissions(CommandSender sender) {
        return Permissions.hardcoreToggle(sender);
    }

    @Override
    protected boolean checkModifyPermissions(CommandSender sender) {
        return Permissions.hardcoreModify(sender);
    }

    @Override
    protected boolean checkEnabled(SkillType skill) {
        if (skill == null) {
            for (SkillType skillType : SkillType.values()) {
                if (!skillType.getHardcoreStatLossEnabled()) {
                    return false;
                }
            }

            return true;
        }

        return skill.getHardcoreStatLossEnabled();
    }

    @Override
    protected void enable(SkillType skill) {
        toggle(true, skill);
    }

    @Override
    protected void disable(SkillType skill) {
        toggle(false, skill);
    }

    @Override
    protected void modify(CommandSender sender, double newPercentage) {
        Config.getInstance().setHardcoreDeathStatPenaltyPercentage(newPercentage);
        sender.sendMessage(LocaleLoader.getString("Hardcore.DeathStatLoss.PercentageChanged", percent.format(newPercentage / 100.0D)));
    }

    private void toggle(boolean enable, SkillType skill) {
        if (skill == null) {
            for (SkillType skillType : SkillType.NON_CHILD_SKILLS()) {
                skillType.setHardcoreStatLossEnabled(enable);
            }
        } else {
            skill.setHardcoreStatLossEnabled(enable);
        }

        FactionsCaribe.getInstance().getServer().broadcastMessage(LocaleLoader.getString("Hardcore.Mode." + (enable ? "Enabled" : "Disabled"), LocaleLoader.getString("Hardcore.DeathStatLoss.Name"), (skill == null ? "all skills" : skill.getName())));
    }
}
