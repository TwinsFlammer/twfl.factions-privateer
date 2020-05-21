package br.com.twinsflammer.factionsprivateer.mcmmo.runnables.commands;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.database.PlayerStat;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MctopCommandAsyncTask extends BukkitRunnable {

    private final CommandSender sender;
    private final SkillType skill;
    private final int page;
    private final boolean useBoard, useChat;

    public MctopCommandAsyncTask(int page, SkillType skill, CommandSender sender, boolean useBoard, boolean useChat) {
        Validate.isTrue(useBoard || useChat, "Attempted to start a rank retrieval with both board and chat off");
        Validate.notNull(sender, "Attempted to start a rank retrieval with no recipient");

        if (useBoard) {
            Validate.isTrue(sender instanceof Player, "Attempted to start a rank retrieval displaying scoreboard to a non-player");
        }

        this.page = page;
        this.skill = skill;
        this.sender = sender;
        this.useBoard = useBoard;
        this.useChat = useChat;
    }

    @Override
    public void run() {
        final List<PlayerStat> userStats = mcMMO.getDatabaseManager().readLeaderboard(skill, page, 10);

        new MctopCommandDisplayTask(userStats, page, skill, sender, useBoard, useChat).runTaskLater(FactionsPrivateer.getInstance(), 1);
    }
}
