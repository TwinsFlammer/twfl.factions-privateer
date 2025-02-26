package br.com.twinsflammer.factionsprivateer.mcmmo.util.scoreboards;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.database.PlayerStat;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.PlayerProfile;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.AbilityType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.child.FamilyTree;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Misc;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.Map;

public class ScoreboardWrapper {

    // Initialization variables
    public final String playerName;
    private final Scoreboard scoreboard;
    private boolean tippedKeep = false;
    private boolean tippedClear = false;

    // Internal usage variables (should exist)
    private ScoreboardManager.SidebarType sidebarType;
    private Objective sidebarObjective;
    private Objective powerObjective;

    // Parameter variables (May be null / invalid)
    private Scoreboard oldBoard = null;
    public String targetPlayer = null;
    public SkillType targetSkill = null;
    private PlayerProfile targetProfile = null;
    public int leaderboardPage = -1;

    private ScoreboardWrapper(String playerName, Scoreboard scoreboard) {
        this.playerName = playerName;
        this.scoreboard = scoreboard;
        sidebarType = ScoreboardManager.SidebarType.NONE;
        sidebarObjective = this.scoreboard.registerNewObjective(ScoreboardManager.SIDEBAR_OBJECTIVE, "dummy");
        powerObjective = this.scoreboard.registerNewObjective(ScoreboardManager.POWER_OBJECTIVE, "dummy");

        if (Config.getInstance().getPowerLevelTagsEnabled()) {
            powerObjective.setDisplayName(ScoreboardManager.TAG_POWER_LEVEL);
            powerObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);

            for (McMMOPlayer mcMMOPlayer : UserManager.getPlayers()) {
                powerObjective.getScore(mcMMOPlayer.getProfile().getPlayerName()).setScore(mcMMOPlayer.getPowerLevel());
            }
        }
    }

    public static ScoreboardWrapper create(Player player) {
        return new ScoreboardWrapper(player.getName(), FactionsPrivateer.getInstance().getServer().getScoreboardManager().getNewScoreboard());
    }

    public BukkitTask updateTask = null;

    private class ScoreboardQuickUpdate extends BukkitRunnable {

        @Override
        public void run() {
            updateSidebar();
            updateTask = null;
        }
    }

    public BukkitTask revertTask = null;

    private class ScoreboardChangeTask extends BukkitRunnable {

        @Override
        public void run() {
            tryRevertBoard();
            revertTask = null;
        }
    }

    public BukkitTask cooldownTask = null;

    private class ScoreboardCooldownTask extends BukkitRunnable {

        @Override
        public void run() {
            // Stop updating if it's no longer something displaying cooldowns
            if (isBoardShown() && (isSkillScoreboard() || isCooldownScoreboard())) {
                doSidebarUpdateSoon();
            } else {
                stopCooldownUpdating();
            }
        }
    }

    public void doSidebarUpdateSoon() {
        if (updateTask == null) {
            // To avoid spamming the scheduler, store the instance and run 2 ticks later
            updateTask = new ScoreboardQuickUpdate().runTaskLater(FactionsPrivateer.getInstance(), 2L);
        }
    }

    private void startCooldownUpdating() {
        if (cooldownTask == null) {
            // Repeat every 5 seconds.
            // Cancels once all cooldowns are done, using stopCooldownUpdating().
            cooldownTask = new ScoreboardCooldownTask().runTaskTimer(FactionsPrivateer.getInstance(), 5 * Misc.TICK_CONVERSION_FACTOR, 5 * Misc.TICK_CONVERSION_FACTOR);
        }
    }

    private void stopCooldownUpdating() {
        if (cooldownTask != null) {
            try {
                cooldownTask.cancel();
            } catch (Throwable ignored) {
            }

            cooldownTask = null;
        }
    }

    public boolean isSkillScoreboard() {
        return sidebarType == ScoreboardManager.SidebarType.SKILL_BOARD;
    }

    public boolean isCooldownScoreboard() {
        return sidebarType == ScoreboardManager.SidebarType.COOLDOWNS_BOARD;
    }

    public boolean isStatsScoreboard() {
        return sidebarType == ScoreboardManager.SidebarType.STATS_BOARD;
    }

    /**
     * Set the old scoreboard, for use in reverting.
     */
    public void setOldScoreboard() {
        Player player = FactionsPrivateer.getInstance().getServer().getPlayerExact(playerName);

        if (player == null) {
            ScoreboardManager.cleanup(this);
            return;
        }

        Scoreboard oldBoard = player.getScoreboard();

        if (oldBoard == scoreboard) { // Already displaying it
            if (this.oldBoard == null) {
                // (Shouldn't happen) Use failsafe value - we're already displaying our board, but we don't have the one we should revert to
                this.oldBoard = FactionsPrivateer.getInstance().getServer().getScoreboardManager().getMainScoreboard();
            }
        } else {
            this.oldBoard = oldBoard;
        }
    }

    public void showBoardWithNoRevert() {
        Player player = FactionsPrivateer.getInstance().getServer().getPlayerExact(playerName);

        if (player == null) {
            ScoreboardManager.cleanup(this);
            return;
        }

        if (revertTask != null) {
            revertTask.cancel();
        }

        player.setScoreboard(scoreboard);
        revertTask = null;
    }

    public void showBoardAndScheduleRevert(int ticks) {
        Player player = FactionsPrivateer.getInstance().getServer().getPlayerExact(playerName);

        if (player == null) {
            ScoreboardManager.cleanup(this);
            return;
        }

        if (revertTask != null) {
            revertTask.cancel();
        }

        player.setScoreboard(scoreboard);
        revertTask = new ScoreboardChangeTask().runTaskLater(FactionsPrivateer.getInstance(), ticks);

        // TODO is there any way to do the time that looks acceptable?
        // player.sendMessage(LocaleLoader.getString("Commands.Scoreboard.Timer", StringUtils.capitalize(sidebarType.toString().toLowerCase()), ticks / 20F));
        PlayerProfile profile = UserManager.getPlayer(player).getProfile();

        if (profile.getScoreboardTipsShown() >= Config.getInstance().getTipsAmount()) {
            return;
        }

        if (!tippedKeep) {
            tippedKeep = true;
            player.sendMessage(LocaleLoader.getString("Commands.Scoreboard.Tip.Keep"));
        } else if (!tippedClear) {
            tippedClear = true;
            player.sendMessage(LocaleLoader.getString("Commands.Scoreboard.Tip.Clear"));
            profile.increaseTipsShown();
        }
    }

    public void tryRevertBoard() {
        Player player = FactionsPrivateer.getInstance().getServer().getPlayerExact(playerName);

        if (player == null) {
            ScoreboardManager.cleanup(this);
            return;
        }

        if (oldBoard != null) {
            if (player.getScoreboard() == scoreboard) {
                player.setScoreboard(oldBoard);
                oldBoard = null;
            } else {
                mcMMO.p.debug("Not reverting scoreboard for " + playerName + " - scoreboard was changed by another plugin (Consider disabling the mcMMO scoreboards if you don't want them!)");
            }
        }

        cancelRevert();

        sidebarType = ScoreboardManager.SidebarType.NONE;
        targetPlayer = null;
        targetSkill = null;
        targetProfile = null;
        leaderboardPage = -1;
    }

    public boolean isBoardShown() {
        Player player = FactionsPrivateer.getInstance().getServer().getPlayerExact(playerName);

        if (player == null) {
            ScoreboardManager.cleanup(this);
            return false;
        }

        return player.getScoreboard() == scoreboard;
    }

    public void cancelRevert() {
        if (revertTask == null) {
            return;
        }

        revertTask.cancel();
        revertTask = null;
    }

    // Board Type Changing 'API' methods
    public void setTypeNone() {
        this.sidebarType = ScoreboardManager.SidebarType.NONE;

        targetPlayer = null;
        targetSkill = null;
        targetProfile = null;
        leaderboardPage = -1;

        loadObjective("");
    }

    public void setTypeSkill(SkillType skill) {
        this.sidebarType = ScoreboardManager.SidebarType.SKILL_BOARD;
        targetSkill = skill;

        targetPlayer = null;
        targetProfile = null;
        leaderboardPage = -1;

        loadObjective(ScoreboardManager.skillLabels.get(skill));
    }

    public void setTypeSelfStats() {
        this.sidebarType = ScoreboardManager.SidebarType.STATS_BOARD;

        targetPlayer = null;
        targetSkill = null;
        targetProfile = null;
        leaderboardPage = -1;

        loadObjective(ScoreboardManager.HEADER_STATS);
    }

    public void setTypeInspectStats(PlayerProfile profile) {
        this.sidebarType = ScoreboardManager.SidebarType.STATS_BOARD;
        targetPlayer = profile.getPlayerName();
        targetProfile = profile;

        targetSkill = null;
        leaderboardPage = -1;

        loadObjective(LocaleLoader.getString("Scoreboard.Header.PlayerInspect", targetPlayer));
    }

    public void setTypeCooldowns() {
        this.sidebarType = ScoreboardManager.SidebarType.COOLDOWNS_BOARD;

        targetPlayer = null;
        targetSkill = null;
        targetProfile = null;
        leaderboardPage = -1;

        loadObjective(ScoreboardManager.HEADER_COOLDOWNS);
    }

    public void setTypeSelfRank() {
        this.sidebarType = ScoreboardManager.SidebarType.RANK_BOARD;
        targetPlayer = null;

        targetSkill = null;
        targetProfile = null;
        leaderboardPage = -1;

        loadObjective(ScoreboardManager.HEADER_RANK);
    }

    public void setTypeInspectRank(String otherPlayer) {
        this.sidebarType = ScoreboardManager.SidebarType.RANK_BOARD;
        targetPlayer = otherPlayer;

        targetSkill = null;
        targetProfile = null;
        leaderboardPage = -1;

        loadObjective(ScoreboardManager.HEADER_RANK);
    }

    public void setTypeTopPower(int page) {
        this.sidebarType = ScoreboardManager.SidebarType.TOP_BOARD;
        leaderboardPage = page;
        targetSkill = null;

        targetPlayer = null;
        targetProfile = null;

        int endPosition = page * 15;
        int startPosition = endPosition - 14;
        loadObjective(String.format("%s (%2d - %2d)", ScoreboardManager.POWER_LEVEL, startPosition, endPosition));
    }

    public void setTypeTop(SkillType skill, int page) {
        this.sidebarType = ScoreboardManager.SidebarType.TOP_BOARD;
        leaderboardPage = page;
        targetSkill = skill;

        targetPlayer = null;
        targetProfile = null;

        int endPosition = page * 15;
        int startPosition = endPosition - 14;
        loadObjective(String.format("%s (%2d - %2d)", ScoreboardManager.skillLabels.get(skill), startPosition, endPosition));
    }

    // Setup for after a board type change
    protected void loadObjective(String displayName) {
        sidebarObjective.unregister();
        sidebarObjective = scoreboard.registerNewObjective(ScoreboardManager.SIDEBAR_OBJECTIVE, "dummy");

        if (displayName.length() > 32) {
            displayName = displayName.substring(0, 32);
        }

        sidebarObjective.setDisplayName(displayName);

        updateSidebar();
        // Do last! Minimize packets!
        sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Load new values into the sidebar.
     */
    private void updateSidebar() {
        try {
            updateTask.cancel();
        } catch (Throwable ignored) {
        } // catch NullPointerException and IllegalStateException and any Error; don't care

        updateTask = null;

        if (sidebarType == ScoreboardManager.SidebarType.NONE) {
            return;
        }

        Player player = FactionsPrivateer.getInstance().getServer().getPlayerExact(playerName);

        if (player == null) {
            ScoreboardManager.cleanup(this);
            return;
        }

        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);

        switch (sidebarType) {
            case NONE:
                break;

            case SKILL_BOARD:
                Validate.notNull(targetSkill);

                if (!targetSkill.isChildSkill()) {
                    int currentXP = mcMMOPlayer.getSkillXpLevel(targetSkill);

                    sidebarObjective.getScore(ScoreboardManager.LABEL_CURRENT_XP).setScore(currentXP);
                    sidebarObjective.getScore(ScoreboardManager.LABEL_REMAINING_XP).setScore(mcMMOPlayer.getXpToLevel(targetSkill) - currentXP);
                } else {
                    for (SkillType parentSkill : FamilyTree.getParents(targetSkill)) {
                        sidebarObjective.getScore(ScoreboardManager.skillLabels.get(parentSkill)).setScore(mcMMOPlayer.getSkillLevel(parentSkill));
                    }
                }

                sidebarObjective.getScore(ScoreboardManager.LABEL_LEVEL).setScore(mcMMOPlayer.getSkillLevel(targetSkill));

                if (targetSkill.getAbility() != null) {
                    boolean stopUpdating;

                    if (targetSkill == SkillType.MINING) {
                        // Special-Case: Mining has two abilities, both with cooldowns
                        Score cooldownSB = sidebarObjective.getScore(ScoreboardManager.abilityLabelsSkill.get(AbilityType.SUPER_BREAKER));
                        Score cooldownBM = sidebarObjective.getScore(ScoreboardManager.abilityLabelsSkill.get(AbilityType.BLAST_MINING));
                        int secondsSB = Math.max(mcMMOPlayer.calculateTimeRemaining(AbilityType.SUPER_BREAKER), 0);
                        int secondsBM = Math.max(mcMMOPlayer.calculateTimeRemaining(AbilityType.BLAST_MINING), 0);

                        cooldownSB.setScore(secondsSB);
                        cooldownBM.setScore(secondsBM);

                        stopUpdating = (secondsSB == 0 && secondsBM == 0);
                    } else {
                        AbilityType ability = targetSkill.getAbility();
                        Score cooldown = sidebarObjective.getScore(ScoreboardManager.abilityLabelsSkill.get(ability));
                        int seconds = Math.max(mcMMOPlayer.calculateTimeRemaining(ability), 0);

                        cooldown.setScore(seconds);

                        stopUpdating = seconds == 0;
                    }

                    if (stopUpdating) {
                        stopCooldownUpdating();
                    } else {
                        startCooldownUpdating();
                    }
                }
                break;

            case COOLDOWNS_BOARD:
                boolean anyCooldownsActive = false;

                for (AbilityType ability : AbilityType.values()) {
                    int seconds = Math.max(mcMMOPlayer.calculateTimeRemaining(ability), 0);

                    if (seconds != 0) {
                        anyCooldownsActive = true;
                    }

                    sidebarObjective.getScore(ScoreboardManager.abilityLabelsColored.get(ability)).setScore(seconds);
                }

                if (anyCooldownsActive) {
                    startCooldownUpdating();
                } else {
                    stopCooldownUpdating();
                }
                break;

            case STATS_BOARD:
                // Select the profile to read from
                PlayerProfile newProfile;

                if (targetProfile != null) {
                    newProfile = targetProfile; // offline
                } else if (targetPlayer == null) {
                    newProfile = mcMMOPlayer.getProfile(); // self
                } else {
                    newProfile = UserManager.getPlayer(targetPlayer).getProfile(); // online
                }

                // Calculate power level here
                int powerLevel = 0;
                for (SkillType skill : SkillType.NON_CHILD_SKILLS()) { // Don't include child skills, makes the list too long
                    int level = newProfile.getSkillLevel(skill);

                    powerLevel += level;

                    // TODO: Verify that this is what we want - calculated in power level but not displayed
                    if (!skill.getPermissions(player)) {
                        continue;
                    }

                    sidebarObjective.getScore(ScoreboardManager.skillLabels.get(skill)).setScore(level);
                }

                sidebarObjective.getScore(ScoreboardManager.LABEL_POWER_LEVEL).setScore(powerLevel);
                break;

            case RANK_BOARD:
            case TOP_BOARD:
                /*
                 * @see #acceptRankData(Map<SkillType, Integer> rank)
                 * @see #acceptLeaderboardData(List<PlayerStat> stats)
                 */
                break;

            default:
                break;
        }
    }

    public void acceptRankData(Map<SkillType, Integer> rankData) {
        Integer rank;
        Player player = FactionsPrivateer.getInstance().getServer().getPlayerExact(playerName);

        for (SkillType skill : SkillType.NON_CHILD_SKILLS()) {
            if (!skill.getPermissions(player)) {
                continue;
            }

            rank = rankData.get(skill);

            if (rank != null) {
                sidebarObjective.getScore(ScoreboardManager.skillLabels.get(skill)).setScore(rank);
            }
        }

        rank = rankData.get(null);

        if (rank != null) {
            sidebarObjective.getScore(ScoreboardManager.LABEL_POWER_LEVEL).setScore(rank);
        }
    }

    public void acceptLeaderboardData(List<PlayerStat> leaderboardData) {
        for (PlayerStat stat : leaderboardData) {
            String name = stat.name;

            if (name.equals(playerName)) {
                name = ChatColor.GOLD + "--You--";
            }

            sidebarObjective.getScore(name).setScore(stat.statVal);
        }
    }

    public void updatePowerLevel(Player player, int newPowerLevel) {
        powerObjective.getScore(player.getName()).setScore(newPowerLevel);
    }
}
