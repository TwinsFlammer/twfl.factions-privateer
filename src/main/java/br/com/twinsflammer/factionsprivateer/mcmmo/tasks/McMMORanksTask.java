package br.com.twinsflammer.factionsprivateer.mcmmo.tasks;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.database.PlayerStat;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class McMMORanksTask implements Runnable {

    private static final HashMap<String, Map<SkillType, Integer>> PLAYER_RANKS = Maps.newHashMap();
    private static final HashMap<SkillType, List<PlayerStat>> SKILL_TOP = Maps.newHashMap();

    public static Map<SkillType, Integer> getPlayerRank(String name) {
        return PLAYER_RANKS.getOrDefault(name, Maps.newHashMap());
    }

    public static PlayerStat getTopSkill(SkillType skill) {
        return getTopSkill(skill, 0);
    }

    public static PlayerStat getTopSkill(SkillType skill, Integer rank) {
        List<PlayerStat> result = SKILL_TOP.getOrDefault(skill, Lists.newArrayList());
        return result.isEmpty() || result.size() <= rank ? null : result.get(rank);
    }

    public static void removePlayer(String name) {
        PLAYER_RANKS.remove(name);
    }

    public static void downloadPlayer(String name) {
        PLAYER_RANKS.put(name, mcMMO.getDatabaseManager().readRank(name));
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> downloadPlayer(player.getName()));
        SkillType.NON_CHILD_SKILLS().forEach(skill -> {
            List<PlayerStat> result = mcMMO.getDatabaseManager().readLeaderboard(skill, 1, 20);
            SKILL_TOP.put(skill, result.stream().limit(10).collect(Collectors.toList()));
        });
        List<PlayerStat> result = mcMMO.getDatabaseManager().readLeaderboard(null, 1, 20);
        SKILL_TOP.put(null, result.stream().limit(10).collect(Collectors.toList()));
    }

}
