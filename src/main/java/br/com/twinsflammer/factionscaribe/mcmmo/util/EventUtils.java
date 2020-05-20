package br.com.twinsflammer.factionscaribe.mcmmo.util;

import br.com.twinsflammer.factionscaribe.mcmmo.events.experience.McMMOPlayerLevelChangeEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.experience.McMMOPlayerLevelDownEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.experience.McMMOPlayerLevelUpEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.experience.McMMOPlayerXpGainEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.fake.FakeBlockBreakEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.fake.FakeBlockDamageEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.fake.FakePlayerAnimationEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.fake.FakePlayerFishEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.hardcore.McMMOPlayerPreDeathPenaltyEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.hardcore.McMMOPlayerStatLossEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.hardcore.McMMOPlayerVampirismEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.party.McMMOPartyLevelUpEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.party.McMMOPartyTeleportEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.party.McMMOPartyXpGainEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.skills.abilities.McMMOPlayerAbilityDeactivateEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.skills.fishing.McMMOPlayerFishingTreasureEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.skills.fishing.McMMOPlayerMagicHunterEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.skills.repair.McMMOPlayerRepairCheckEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.skills.secondaryabilities.SecondaryAbilityEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.events.skills.unarmed.McMMOPlayerDisarmEvent;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.party.Party;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.PlayerProfile;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.AbilityType;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SecondaryAbility;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.XPGainReason;

import java.util.HashMap;
import java.util.Map;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

public class EventUtils {

    public static McMMOPlayerAbilityActivateEvent callPlayerAbilityActivateEvent(Player player, SkillType skill) {
        McMMOPlayerAbilityActivateEvent event = new McMMOPlayerAbilityActivateEvent(player, skill);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        return event;
    }

    public static SecondaryAbilityEvent callSecondaryAbilityEvent(Player player, SecondaryAbility secondaryAbility) {
        SecondaryAbilityEvent event = new SecondaryAbilityEvent(player, secondaryAbility);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        return event;
    }

    public static FakePlayerAnimationEvent callFakeArmSwingEvent(Player player) {
        FakePlayerAnimationEvent event = new FakePlayerAnimationEvent(player);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        return event;
    }

    public static boolean handleLevelChangeEvent(Player player, SkillType skill, int levelsChanged, float xpRemoved, boolean isLevelUp, XPGainReason xpGainReason) {
        McMMOPlayerLevelChangeEvent event = isLevelUp ? new McMMOPlayerLevelUpEvent(player, skill, levelsChanged, xpGainReason) : new McMMOPlayerLevelDownEvent(player, skill, levelsChanged, xpGainReason);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        boolean isCancelled = event.isCancelled();

        if (isCancelled) {
            PlayerProfile profile = UserManager.getPlayer(player).getProfile();

            profile.modifySkill(skill, profile.getSkillLevel(skill) - (isLevelUp ? levelsChanged : -levelsChanged));
            profile.addXp(skill, xpRemoved);
        }

        return !isCancelled;
    }

    /**
     * Simulate a block break event.
     *
     * @param block The block to break
     * @param player The player breaking the block
     * @param shouldArmSwing true if an armswing event should be fired, false
     * otherwise
     * @return true if the event wasn't cancelled, false otherwise
     */
    public static boolean simulateBlockBreak(Block block, Player player, boolean shouldArmSwing) {
        PluginManager pluginManager = FactionsCaribe.getInstance().getServer().getPluginManager();

        // Support for NoCheat
        if (shouldArmSwing) {
            callFakeArmSwingEvent(player);
        }

        FakeBlockDamageEvent damageEvent = new FakeBlockDamageEvent(player, block, player.getItemInHand(), true);
        pluginManager.callEvent(damageEvent);

        FakeBlockBreakEvent breakEvent = new FakeBlockBreakEvent(block, player);
        pluginManager.callEvent(breakEvent);

        return !damageEvent.isCancelled() && !breakEvent.isCancelled();
    }

    public static void handlePartyTeleportEvent(Player teleportingPlayer, Player targetPlayer) {
        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(teleportingPlayer);

        McMMOPartyTeleportEvent event = new McMMOPartyTeleportEvent(teleportingPlayer, targetPlayer, mcMMOPlayer.getParty().getName());
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        teleportingPlayer.teleport(targetPlayer);

        teleportingPlayer.sendMessage(LocaleLoader.getString("Party.Teleport.Player", targetPlayer.getName()));
        targetPlayer.sendMessage(LocaleLoader.getString("Party.Teleport.Target", teleportingPlayer.getName()));

        mcMMOPlayer.getPartyTeleportRecord().actualizeLastUse();
    }

    public static boolean handlePartyXpGainEvent(Party party, float xpGained) {
        McMMOPartyXpGainEvent event = new McMMOPartyXpGainEvent(party, xpGained);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        boolean isCancelled = event.isCancelled();

        if (!isCancelled) {
            party.addXp(event.getRawXpGained());
        }

        return !isCancelled;
    }

    public static boolean handlePartyLevelChangeEvent(Party party, int levelsChanged, float xpRemoved) {
        McMMOPartyLevelUpEvent event = new McMMOPartyLevelUpEvent(party, levelsChanged);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        boolean isCancelled = event.isCancelled();

        if (isCancelled) {

            party.setLevel(party.getLevel() + levelsChanged);
            party.addXp(xpRemoved);
        }

        return !isCancelled;
    }

    public static boolean handleXpGainEvent(Player player, SkillType skill, float xpGained, XPGainReason xpGainReason) {
        McMMOPlayerXpGainEvent event = new McMMOPlayerXpGainEvent(player, skill, xpGained, xpGainReason);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        boolean isCancelled = event.isCancelled();

        if (!isCancelled) {
            UserManager.getPlayer(player).addXp(skill, event.getRawXpGained());
            UserManager.getPlayer(player).getProfile().registerXpGain(skill, event.getRawXpGained());
        }

        return !isCancelled;
    }

    public static boolean handleStatsLossEvent(Player player, HashMap<String, Integer> levelChanged, HashMap<String, Float> experienceChanged) {
        McMMOPlayerStatLossEvent event = new McMMOPlayerStatLossEvent(player, levelChanged, experienceChanged);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        boolean isCancelled = event.isCancelled();

        if (!isCancelled) {
            levelChanged = event.getLevelChanged();
            experienceChanged = event.getExperienceChanged();
            PlayerProfile playerProfile = UserManager.getPlayer(player).getProfile();

            for (SkillType skillType : SkillType.NON_CHILD_SKILLS()) {
                String skillName = skillType.toString();
                int playerSkillLevel = playerProfile.getSkillLevel(skillType);

                playerProfile.modifySkill(skillType, playerSkillLevel - levelChanged.get(skillName));
                playerProfile.removeXp(skillType, experienceChanged.get(skillName));

                if (playerProfile.getSkillXpLevel(skillType) < 0) {
                    playerProfile.setSkillXpLevel(skillType, 0);
                }

                if (playerProfile.getSkillLevel(skillType) < 0) {
                    playerProfile.modifySkill(skillType, 0);
                }
            }
        }

        return !isCancelled;
    }

    public static boolean handleVampirismEvent(Player killer, Player victim, HashMap<String, Integer> levelChanged, HashMap<String, Float> experienceChanged) {
        McMMOPlayerVampirismEvent eventKiller = new McMMOPlayerVampirismEvent(killer, false, levelChanged, experienceChanged);
        McMMOPlayerVampirismEvent eventVictim = new McMMOPlayerVampirismEvent(victim, true, levelChanged, experienceChanged);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(eventKiller);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(eventVictim);

        boolean isCancelled = eventKiller.isCancelled() || eventVictim.isCancelled();

        if (!isCancelled) {
            HashMap<String, Integer> levelChangedKiller = eventKiller.getLevelChanged();
            HashMap<String, Float> experienceChangedKiller = eventKiller.getExperienceChanged();

            HashMap<String, Integer> levelChangedVictim = eventVictim.getLevelChanged();
            HashMap<String, Float> experienceChangedVictim = eventVictim.getExperienceChanged();

            McMMOPlayer killerPlayer = UserManager.getPlayer(killer);
            PlayerProfile victimProfile = UserManager.getPlayer(victim).getProfile();

            for (SkillType skillType : SkillType.NON_CHILD_SKILLS()) {
                String skillName = skillType.toString();
                int victimSkillLevel = victimProfile.getSkillLevel(skillType);

                killerPlayer.addLevels(skillType, levelChangedKiller.get(skillName));
                killerPlayer.beginUnsharedXpGain(skillType, experienceChangedKiller.get(skillName), XPGainReason.VAMPIRISM);

                victimProfile.modifySkill(skillType, victimSkillLevel - levelChangedVictim.get(skillName));
                victimProfile.removeXp(skillType, experienceChangedVictim.get(skillName));

                if (victimProfile.getSkillXpLevel(skillType) < 0) {
                    victimProfile.setSkillXpLevel(skillType, 0);
                }

                if (victimProfile.getSkillLevel(skillType) < 0) {
                    victimProfile.modifySkill(skillType, 0);
                }
            }
        }

        return !isCancelled;
    }

    public static McMMOPlayerAbilityDeactivateEvent callAbilityDeactivateEvent(Player player, AbilityType ability) {
        McMMOPlayerAbilityDeactivateEvent event = new McMMOPlayerAbilityDeactivateEvent(player, SkillType.byAbility(ability));
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        return event;
    }

    public static McMMOPlayerFishingTreasureEvent callFishingTreasureEvent(Player player, ItemStack treasureDrop, int treasureXp, Map<Enchantment, Integer> enchants) {
        McMMOPlayerFishingTreasureEvent event = enchants.isEmpty() ? new McMMOPlayerFishingTreasureEvent(player, treasureDrop, treasureXp) : new McMMOPlayerMagicHunterEvent(player, treasureDrop, treasureXp, enchants);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        return event;
    }

    public static FakePlayerFishEvent callFakeFishEvent(Player player, Fish hook) {
        FakePlayerFishEvent event = new FakePlayerFishEvent(player, null, hook, PlayerFishEvent.State.FISHING);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        return event;
    }

    public static McMMOPlayerRepairCheckEvent callRepairCheckEvent(Player player, short durability, ItemStack repairMaterial, ItemStack repairedObject) {
        McMMOPlayerRepairCheckEvent event = new McMMOPlayerRepairCheckEvent(player, durability, repairMaterial, repairedObject);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        return event;
    }

    public static McMMOPlayerPreDeathPenaltyEvent callPreDeathPenaltyEvent(Player player) {
        McMMOPlayerPreDeathPenaltyEvent event = new McMMOPlayerPreDeathPenaltyEvent(player);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        return event;
    }

    public static McMMOPlayerDisarmEvent callDisarmEvent(Player defender) {
        McMMOPlayerDisarmEvent event = new McMMOPlayerDisarmEvent(defender);
        FactionsCaribe.getInstance().getServer().getPluginManager().callEvent(event);

        return event;
    }
}
