package com.redefocus.factionscaribe.mcmmo.commands.hardcore;

import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.util.Permissions;
import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.command.CommandSender;

public class VampirismCommand extends HardcoreModeCommand {

    @Override
    protected boolean checkTogglePermissions(CommandSender sender) {
        return Permissions.vampirismToggle(sender);
    }

    @Override
    protected boolean checkModifyPermissions(CommandSender sender) {
        return Permissions.vampirismModify(sender);
    }

    @Override
    protected boolean checkEnabled(SkillType skill) {
        if (skill == null) {
            for (SkillType skillType : SkillType.values()) {
                if (!skillType.getHardcoreVampirismEnabled()) {
                    return false;
                }
            }

            return true;
        }

        return skill.getHardcoreVampirismEnabled();
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
        Config.getInstance().setHardcoreVampirismStatLeechPercentage(newPercentage);
        sender.sendMessage(LocaleLoader.getString("Hardcore.Vampirism.PercentageChanged", percent.format(newPercentage / 100.0D)));
    }

    private void toggle(boolean enable, SkillType skill) {
        if (skill == null) {
            for (SkillType skillType : SkillType.NON_CHILD_SKILLS()) {
                skillType.setHardcoreVampirismEnabled(enable);
            }
        } else {
            skill.setHardcoreVampirismEnabled(enable);
        }

        FactionsCaribe.getInstance().getServer().broadcastMessage(LocaleLoader.getString("Hardcore.Mode." + (enable ? "Enabled" : "Disabled"), LocaleLoader.getString("Hardcore.Vampirism.Name"), (skill == null ? "all skills" : skill)));
    }
}
