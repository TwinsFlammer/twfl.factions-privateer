package br.com.twinsflammer.factionsprivateer.mcmmo.runnables.database;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.experience.ExperienceConfig;
import br.com.twinsflammer.factionsprivateer.mcmmo.database.DatabaseManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Misc;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.experience.FormulaType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.PlayerProfile;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class FormulaConversionTask extends BukkitRunnable {

    private CommandSender sender;
    private FormulaType formulaType;

    public FormulaConversionTask(CommandSender sender, FormulaType formulaType) {
        this.sender = sender;
        this.formulaType = formulaType;
    }

    @Override
    public void run() {
        int convertedUsers = 0;
        long startMillis = System.currentTimeMillis();
        for (String playerName : mcMMO.getDatabaseManager().getStoredUsers()) {
            McMMOPlayer mcMMOPlayer = UserManager.getOfflinePlayer(playerName);
            PlayerProfile profile;

            // If the mcMMOPlayer doesn't exist, create a temporary profile and check if it's present in the database. If it's not, abort the process.
            if (mcMMOPlayer == null) {
                profile = mcMMO.getDatabaseManager().loadPlayerProfile(playerName, false);

                if (!profile.isLoaded()) {
                    mcMMO.p.debug("Profile not loaded.");
                    continue;
                }

                editValues(profile);
                // Since this is a temporary profile, we save it here.
                profile.scheduleAsyncSave();
            } else {
                profile = mcMMOPlayer.getProfile();
                editValues(profile);
            }
            convertedUsers++;
            Misc.printProgress(convertedUsers, DatabaseManager.progressInterval, startMillis);
        }
        mcMMO.getFormulaManager().setPreviousFormulaType(formulaType);

        sender.sendMessage(LocaleLoader.getString("Commands.mcconvert.Experience.Finish", formulaType.toString()));
    }

    private void editValues(PlayerProfile profile) {
        mcMMO.p.debug("========================================================================");
        mcMMO.p.debug("Conversion report for " + profile.getPlayerName() + ":");
        for (SkillType skillType : SkillType.NON_CHILD_SKILLS()) {
            int oldLevel = profile.getSkillLevel(skillType);
            int oldXPLevel = profile.getSkillXpLevel(skillType);
            int totalOldXP = mcMMO.getFormulaManager().calculateTotalExperience(oldLevel, oldXPLevel);

            if (totalOldXP == 0) {
                continue;
            }

            int[] newExperienceValues = mcMMO.getFormulaManager().calculateNewLevel(skillType, (int) Math.floor(totalOldXP / ExperienceConfig.getInstance().getExpModifier()), formulaType);
            int newLevel = newExperienceValues[0];
            int newXPlevel = newExperienceValues[1];

            mcMMO.p.debug("  Skill: " + skillType.toString());

            mcMMO.p.debug("    OLD:");
            mcMMO.p.debug("      Level: " + oldLevel);
            mcMMO.p.debug("      XP " + oldXPLevel);
            mcMMO.p.debug("      Total XP " + totalOldXP);

            mcMMO.p.debug("    NEW:");
            mcMMO.p.debug("      Level " + newLevel);
            mcMMO.p.debug("      XP " + newXPlevel);
            mcMMO.p.debug("------------------------------------------------------------------------");

            profile.modifySkill(skillType, newLevel);
            profile.setSkillXpLevel(skillType, newXPlevel);
        }
    }
}
