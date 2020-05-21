package br.com.twinsflammer.factionsprivateer.mcmmo.commands.hardcore;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Permissions;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
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

        FactionsPrivateer.getInstance().getServer().broadcastMessage(LocaleLoader.getString("Hardcore.Mode." + (enable ? "Enabled" : "Disabled"), LocaleLoader.getString("Hardcore.DeathStatLoss.Name"), (skill == null ? "all skills" : skill.getName())));
    }
}
