package com.redefocus.factionscaribe.mcmmo.runnables.commands;

import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
import java.util.Map;

import com.redefocus.factionscaribe.FactionsCaribe;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class McrankCommandAsyncTask extends BukkitRunnable {

    private final String playerName;
    private final CommandSender sender;
    private final boolean useBoard, useChat;

    public McrankCommandAsyncTask(String playerName, CommandSender sender, boolean useBoard, boolean useChat) {
        Validate.isTrue(useBoard || useChat, "Attempted to start a rank retrieval with both board and chat off");
        Validate.notNull(sender, "Attempted to start a rank retrieval with no recipient");

        if (useBoard) {
            Validate.isTrue(sender instanceof Player, "Attempted to start a rank retrieval displaying scoreboard to a non-player");
        }

        this.playerName = playerName;
        this.sender = sender;
        this.useBoard = useBoard;
        this.useChat = useChat;
    }

    @Override
    public void run() {
        Map<SkillType, Integer> skills = mcMMO.getDatabaseManager().readRank(playerName);

        new McrankCommandDisplayTask(skills, sender, playerName, useBoard, useChat).runTaskLater(FactionsCaribe.getInstance(), 1);
    }
}
