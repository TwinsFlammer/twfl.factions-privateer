package br.com.twinsflammer.factionsprivateer.mcmmo.skills.child;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.AutoUpdateConfigLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.EnumSet;

public class ChildConfig extends AutoUpdateConfigLoader {

    public ChildConfig() {
        super("child.yml");
        loadKeys();
    }

    @Override
    protected void loadKeys() {
        config.setDefaults(YamlConfiguration.loadConfiguration(FactionsPrivateer.getInstance().getResource("child.yml")));

        FamilyTree.clearRegistrations(); // when reloading, need to clear statics

        for (SkillType skill : SkillType.CHILD_SKILLS()) {
            plugin.debug("Finding parents of " + skill.name());

            EnumSet<SkillType> parentSkills = EnumSet.noneOf(SkillType.class);
            boolean useDefaults = false; // If we had an error we back out and use defaults

            for (String name : config.getStringList(StringUtils.getCapitalized(skill.name()))) {
                try {
                    SkillType parentSkill = SkillType.valueOf(name.toUpperCase());
                    FamilyTree.enforceNotChildSkill(parentSkill);
                    parentSkills.add(parentSkill);
                } catch (IllegalArgumentException ex) {
                    FactionsPrivateer.getInstance().getLogger().warning(name + " is not a valid skill type, or is a child skill!");
                    useDefaults = true;
                    break;
                }
            }

            if (useDefaults) {
                parentSkills.clear();
                for (String name : config.getDefaults().getStringList(StringUtils.getCapitalized(skill.name()))) {
                    /* We do less checks in here because it's from inside our jar.
                     * If they're dedicated enough to have modified it, they can have the errors it may produce.
                     * Alternatively, this can be used to allow child skills to be parent skills, provided there are no circular dependencies this is an advanced sort of configuration.
                     */
                    parentSkills.add(SkillType.valueOf(name.toUpperCase()));
                }
            }

            // Register them
            for (SkillType parentSkill : parentSkills) {
                plugin.debug("Registering " + parentSkill.name() + " as parent of " + skill.name());
                FamilyTree.registerParent(skill, parentSkill);
            }
        }

        FamilyTree.closeRegistration();
    }
}
