package br.com.twinsflammer.factionsprivateer.mcmmo.util.commands;

import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.StringUtils;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import lombok.NoArgsConstructor;
import org.bukkit.command.PluginCommand;

@NoArgsConstructor
public final class CommandRegistrationManager {

    private static String permissionsMessage = LocaleLoader.getString("mcMMO.NoPermission");

    private static void registerSkillCommands() {
        for (SkillType skill : SkillType.values()) {
            if (!skill.isActive()) {
                continue;
            }

            registerSkillCommand(skill);
        }
    }

    public static void registerSkillCommand(SkillType skill) {
        String commandName = skill.toString().toLowerCase();
        String localizedName = skill.getName().toLowerCase();

        PluginCommand command;

        command = FactionsPrivateer.getInstance().getCommand(commandName);
        command.setDescription(LocaleLoader.getString("Commands.Description.Skill", StringUtils.getCapitalized(localizedName)));
        command.setPermission("mcmmo.commands." + commandName);
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.0", localizedName));
        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.2", localizedName, "?", "[" + LocaleLoader.getString("Commands.Usage.Page") + "]"));
    }

    public static void registerCommands() {
    }
}
