package br.com.twinsflammer.factionsprivateer.mcmmo.commands.hardcore;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Permissions;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
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

        FactionsPrivateer.getInstance().getServer().broadcastMessage(LocaleLoader.getString("Hardcore.Mode." + (enable ? "Enabled" : "Disabled"), LocaleLoader.getString("Hardcore.Vampirism.Name"), (skill == null ? "all skills" : skill)));
    }
}
