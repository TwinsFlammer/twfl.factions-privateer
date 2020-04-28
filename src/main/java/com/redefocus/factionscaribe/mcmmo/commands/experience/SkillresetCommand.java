package com.redefocus.factionscaribe.mcmmo.commands.experience;

import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.datatypes.player.PlayerProfile;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.XPGainReason;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.mcmmo.util.EventUtils;
import com.redefocus.factionscaribe.mcmmo.util.Permissions;
import com.redefocus.factionscaribe.mcmmo.util.commands.CommandUtils;
import com.redefocus.factionscaribe.mcmmo.util.player.UserManager;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

/**
 * This class mirrors the structure of ExperienceCommand, except the
 * value/quantity argument is removed.
 */
public class SkillresetCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SkillType skill;
        switch (args.length) {
            case 1:
                if (CommandUtils.noConsoleUsage(sender)) {
                    return true;
                }

                if (!permissionsCheckSelf(sender)) {
                    sender.sendMessage(command.getPermissionMessage());
                    return true;
                }

                if (!validateArguments(sender, args[0])) {
                    return true;
                }

                if (args[0].equalsIgnoreCase("all")) {
                    skill = null;
                } else {
                    skill = SkillType.getSkill(args[1]);
                }

                editValues((Player) sender, UserManager.getPlayer(sender.getName()).getProfile(), skill);
                return true;

            case 2:
                if (!permissionsCheckOthers(sender)) {
                    sender.sendMessage(command.getPermissionMessage());
                    return true;
                }

                if (!validateArguments(sender, args[1])) {
                    return true;
                }

                if (args[1].equalsIgnoreCase("all")) {
                    skill = null;
                } else {
                    skill = SkillType.getSkill(args[1]);
                }

                String playerName = CommandUtils.getMatchedPlayerName(args[0]);
                McMMOPlayer mcMMOPlayer = UserManager.getOfflinePlayer(playerName);

                // If the mcMMOPlayer doesn't exist, create a temporary profile and check if it's present in the database. If it's not, abort the process.
                if (mcMMOPlayer == null) {
                    UUID uuid = null;
                    OfflinePlayer player = FactionsCaribe.getInstance().getServer().getOfflinePlayer(playerName);
                    if (player != null) {
                        uuid = player.getUniqueId();
                    }
                    PlayerProfile profile = mcMMO.getDatabaseManager().loadPlayerProfile(playerName, uuid, false);

                    if (CommandUtils.unloadedProfile(sender, profile)) {
                        return true;
                    }

                    editValues(null, profile, skill);
                } else {
                    editValues(mcMMOPlayer.getPlayer(), mcMMOPlayer.getProfile(), skill);
                }

                handleSenderMessage(sender, playerName, skill);
                return true;

            default:
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1:
                List<String> playerNames = CommandUtils.getOnlinePlayerNames(sender);
                return StringUtil.copyPartialMatches(args[0], playerNames, new ArrayList<String>(playerNames.size()));
            case 2:
                return StringUtil.copyPartialMatches(args[1], SkillType.SKILL_NAMES(), new ArrayList<String>(SkillType.SKILL_NAMES().size()));
            default:
                return ImmutableList.of();
        }
    }

    protected void handleCommand(Player player, PlayerProfile profile, SkillType skill) {
        int levelsRemoved = profile.getSkillLevel(skill);
        float xpRemoved = profile.getSkillXpLevelRaw(skill);

        profile.modifySkill(skill, 0);

        if (player == null) {
            profile.scheduleAsyncSave();
            return;
        }

        EventUtils.handleLevelChangeEvent(player, skill, levelsRemoved, xpRemoved, false, XPGainReason.COMMAND);
    }

    protected boolean permissionsCheckSelf(CommandSender sender) {
        return Permissions.skillreset(sender);
    }

    protected boolean permissionsCheckOthers(CommandSender sender) {
        return Permissions.skillresetOthers(sender);
    }

    protected void handlePlayerMessageAll(Player player) {
        player.sendMessage(LocaleLoader.getString("Commands.Reset.All"));
    }

    protected void handlePlayerMessageSkill(Player player, SkillType skill) {
        player.sendMessage(LocaleLoader.getString("Commands.Reset.Single", skill.getName()));
    }

    private boolean validateArguments(CommandSender sender, String skillName) {
        return skillName.equalsIgnoreCase("all") || !CommandUtils.isInvalidSkill(sender, skillName);
    }

    protected static void handleSenderMessage(CommandSender sender, String playerName, SkillType skill) {
        if (skill == null) {
            sender.sendMessage(LocaleLoader.getString("Commands.addlevels.AwardAll.2", playerName));
        } else {
            sender.sendMessage(LocaleLoader.getString("Commands.addlevels.AwardSkill.2", skill.getName(), playerName));
        }
    }

    protected void editValues(Player player, PlayerProfile profile, SkillType skill) {
        if (skill == null) {
            for (SkillType skillType : SkillType.NON_CHILD_SKILLS()) {
                handleCommand(player, profile, skillType);
            }

            if (player != null) {
                handlePlayerMessageAll(player);
            }
        } else {
            handleCommand(player, profile, skill);

            if (player != null) {
                handlePlayerMessageSkill(player, skill);
            }
        }
    }
}
