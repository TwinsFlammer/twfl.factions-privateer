package com.redefocus.factionscaribe.mcmmo.runnables.skills;

import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.AbilityType;
import com.redefocus.factionscaribe.mcmmo.util.EventUtils;
import com.redefocus.factionscaribe.mcmmo.util.Misc;
import com.redefocus.factionscaribe.mcmmo.util.skills.ParticleEffectUtils;
import com.redefocus.factionscaribe.mcmmo.util.skills.PerksUtils;
import com.redefocus.factionscaribe.mcmmo.util.skills.SkillUtils;
import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AbilityDisableTask extends BukkitRunnable {

    private McMMOPlayer mcMMOPlayer;
    private AbilityType ability;

    public AbilityDisableTask(McMMOPlayer mcMMOPlayer, AbilityType ability) {
        this.mcMMOPlayer = mcMMOPlayer;
        this.ability = ability;
    }

    @Override
    public void run() {
        if (!mcMMOPlayer.getAbilityMode(ability)) {
            return;
        }

        Player player = mcMMOPlayer.getPlayer();

        switch (ability) {
            case SUPER_BREAKER:
            case GIGA_DRILL_BREAKER:
                SkillUtils.handleAbilitySpeedDecrease(player);
            // Fallthrough

            case BERSERK:
                if (Config.getInstance().getRefreshChunksEnabled()) {
                    resendChunkRadiusAt(player, 1);
                }
            // Fallthrough

            default:
                break;
        }

        EventUtils.callAbilityDeactivateEvent(player, ability);

        mcMMOPlayer.setAbilityMode(ability, false);
        mcMMOPlayer.setAbilityInformed(ability, false);

        ParticleEffectUtils.playAbilityDisabledEffect(player);

        if (mcMMOPlayer.useChatNotifications()) {
            player.sendMessage(ability.getAbilityOff());
        }

        SkillUtils.sendSkillMessage(player, ability.getAbilityPlayerOff(player));
        new AbilityCooldownTask(mcMMOPlayer, ability).runTaskLaterAsynchronously(FactionsCaribe.getInstance(), PerksUtils.handleCooldownPerks(player, ability.getCooldown()) * Misc.TICK_CONVERSION_FACTOR);
    }

    private void resendChunkRadiusAt(Player player, int radius) {
        Chunk chunk = player.getLocation().getChunk();
        World world = player.getWorld();

        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        for (int x = chunkX - radius; x < chunkX + radius; x++) {
            for (int z = chunkZ - radius; z < chunkZ + radius; z++) {
                world.refreshChunk(x, z);
            }
        }
    }
}
