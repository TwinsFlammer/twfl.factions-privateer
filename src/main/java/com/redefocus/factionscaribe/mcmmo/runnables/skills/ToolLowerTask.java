package com.redefocus.factionscaribe.mcmmo.runnables.skills;

import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.ToolType;
import org.bukkit.scheduler.BukkitRunnable;

public class ToolLowerTask extends BukkitRunnable {

    private McMMOPlayer mcMMOPlayer;
    private ToolType tool;

    public ToolLowerTask(McMMOPlayer mcMMOPlayer, ToolType tool) {
        this.mcMMOPlayer = mcMMOPlayer;
        this.tool = tool;
    }

    @Override
    public void run() {
        if (!mcMMOPlayer.getToolPreparationMode(tool)) {
            return;
        }

        mcMMOPlayer.setToolPreparationMode(tool, false);

        if (Config.getInstance().getAbilityMessagesEnabled()) {
            mcMMOPlayer.getPlayer().sendMessage(tool.getLowerTool());
        }
    }
}
