package br.com.twinsflammer.factionsprivateer.mcmmo.api;

import br.com.twinsflammer.factionsprivateer.economy.manager.EconomyManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.database.DatabaseManagerFactory;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.database.PlayerStat;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author SrGutyerrez
 */
public class McMMoAPI {
    public static String getTopSkillName(SkillType skillType, int position) {
        String name = "§7Ninguém";

        List<PlayerStat> playerStats = mcMMO.getDatabaseManager().readLeaderboard(skillType, 1, position);

        int i = 1;

        for (PlayerStat playerStat : playerStats) {
            if (!(i > position)) {

                if (i == position) {
                    name = playerStat.name;
                }

                i++;
            }
        }

        return name;
    }

    public static int getTopSkillValue(SkillType skillType, int poition) {
        int value = 0;

        List<PlayerStat> playerStats = mcMMO.getDatabaseManager().readLeaderboard(skillType, 1, poition);

        int i = 1;

        for (PlayerStat playerStat : playerStats) {
            if (!(i > poition)) {

                if (i == poition) {
                    value = playerStat.statVal;
                }

                i++;
            }
        }

        return value;
    }

    public static String getTopAllName(int position) {
        String name = "§7Ninguém";

        List<PlayerStat> playerStats = DatabaseManagerFactory.getDatabaseManager().readLeaderboard(null, 1, position);

        int i = 1;

        for (PlayerStat playerStat : playerStats) {
            if (!(i > position)) {
                if (i == position) {
                    name = playerStat.name;
                }
                i++;
            }
        }

        return name;
    }

    public static int getTopAllValue(int position) {
        int value = 0;

        List<PlayerStat> playerStats = DatabaseManagerFactory.getDatabaseManager().readLeaderboard(null, 1, position);

        int i = 1;

        for (PlayerStat playerStat : playerStats) {
            if (!(i > position)) {

                if (i == position) {
                    value = playerStat.statVal;
                }

                i++;
            }
        }

        return value;
    }

    public static int getAllLevel(UUID uniqueId) {
        int level = 0;

        for (String skillName : SkillAPI.getSkills()) {
            level += ExperienceAPI.getLevelOffline(uniqueId, skillName);
        }

        return level;
    }

    public static int getLevel(SkillType type, UUID uniqueId) {
        return ExperienceAPI.getLevelOffline(uniqueId, type.getName());
    }

    public static String getPosition(String targetName) {
        String position = "Indefinido.";

        Map<SkillType, Integer> skills = mcMMO.getDatabaseManager().readRank(targetName);
        if (skills.get(null) != null) position = EconomyManager.format(skills.get(null).doubleValue());

        return position;
    }

    public static Integer getPosition(SkillType skillType, String targetName) {
        Integer position = null;

        Map<SkillType, Integer> skills = mcMMO.getDatabaseManager().readRank(targetName);

        if (skills.get(skillType) != null) position = skills.get(skillType);

        return position;
    }

    public static Integer getExperience(SkillType skillType, UUID uniqueId) {
        return ExperienceAPI.getLevelOffline(uniqueId, skillType.getName());
    }

    public static Integer getNeedExperience(SkillType skillType, UUID uniqueId) {
        return ExperienceAPI.getOfflineXPToNextLevel(uniqueId, skillType.getName());
    }
}
