package br.com.twinsflammer.factionsprivateer.mcmmo.runnables.commands;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.scoreboards.ScoreboardManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

/**
 * Display the results of McrankCommandAsyncTask to the sender.
 */
public class McrankCommandDisplayTask extends BukkitRunnable {

    private final Map<SkillType, Integer> skills;
    private final CommandSender sender;
    private final String playerName;
    private final boolean useBoard, useChat;

    McrankCommandDisplayTask(Map<SkillType, Integer> skills, CommandSender sender, String playerName, boolean useBoard, boolean useChat) {
        this.skills = skills;
        this.sender = sender;
        this.playerName = playerName;
        this.useBoard = useBoard;
        this.useChat = useChat;
    }

    @Override
    public void run() {
        if (useBoard) {
            displayBoard();
        }

        if (useChat) {
            displayChat();
        }
        ((Player) sender).removeMetadata(mcMMO.databaseCommandKey, FactionsPrivateer.getInstance());
    }

    private void displayChat() {
        Player player = FactionsPrivateer.getInstance().getServer().getPlayerExact(playerName);
        Integer rank;

        sender.sendMessage(LocaleLoader.getString("Commands.mcrank.Heading"));
        sender.sendMessage(LocaleLoader.getString("Commands.mcrank.Player", playerName));

        for (SkillType skill : SkillType.NON_CHILD_SKILLS()) {
            if (!skill.getPermissions(player)) {
                continue;
            }

            rank = skills.get(skill);
            sender.sendMessage(LocaleLoader.getString("Commands.mcrank.Skill", skill.getName(), (rank == null ? LocaleLoader.getString("Commands.mcrank.Unranked") : rank)));
        }

        rank = skills.get(null);
        sender.sendMessage(LocaleLoader.getString("Commands.mcrank.Overall", (rank == null ? LocaleLoader.getString("Commands.mcrank.Unranked") : rank)));
    }

    public void displayBoard() {
        if (sender.getName().equalsIgnoreCase(playerName)) {
            ScoreboardManager.showPlayerRankScoreboard((Player) sender, skills);
        } else {
            ScoreboardManager.showPlayerRankScoreboardOthers((Player) sender, playerName, skills);
        }
    }
}
