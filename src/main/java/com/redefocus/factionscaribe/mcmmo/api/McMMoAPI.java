package com.redefocus.factionscaribe.mcmmo.api;

import com.redefocus.factionscaribe.economy.manager.EconomyManager;
import com.redefocus.factionscaribe.mcmmo.database.DatabaseManagerFactory;
import com.redefocus.factionscaribe.mcmmo.datatypes.database.PlayerStat;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.mcMMO;

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

    public static String getPosition(SkillType skillType, String targetName) {
        String position = "Indefinido.";

        Map<SkillType, Integer> skills = mcMMO.getDatabaseManager().readRank(targetName);
        if (skills.get(skillType) != null) position = EconomyManager.format(skills.get(skillType).doubleValue());

        return position;
    }

    public static Integer getExperience(SkillType skillType, UUID uniqueId) {
        return ExperienceAPI.getLevelOffline(uniqueId, skillType.getName());
    }

    public static Integer getNeedExperience(SkillType skillType, UUID uniqueId) {
        return ExperienceAPI.getOfflineXPToNextLevel(uniqueId, skillType.getName());
    }
}
