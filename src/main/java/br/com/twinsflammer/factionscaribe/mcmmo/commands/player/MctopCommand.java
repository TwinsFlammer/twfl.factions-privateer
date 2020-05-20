package br.com.twinsflammer.factionscaribe.mcmmo.commands.player;

import br.com.twinsflammer.factionscaribe.mcmmo.config.Config;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.mcMMO;
import br.com.twinsflammer.factionscaribe.mcmmo.runnables.commands.MctopCommandAsyncTask;
import br.com.twinsflammer.factionscaribe.mcmmo.util.Permissions;
import br.com.twinsflammer.factionscaribe.mcmmo.util.StringUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.commands.CommandUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.StringUtil;

public class MctopCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SkillType skill = null;

        switch (args.length) {
            case 0:
                display(1, skill, sender, command);
                return true;

            case 1:
                if (StringUtils.isInt(args[0])) {
                    display(Math.abs(Integer.parseInt(args[0])), skill, sender, command);
                    return true;
                }

                skill = extractSkill(sender, args[0]);

                if (skill == null) {
                    return true;
                }

                display(1, skill, sender, command);
                return true;

            case 2:
                if (CommandUtils.isInvalidInteger(sender, args[1])) {
                    return true;
                }

                skill = extractSkill(sender, args[0]);

                if (skill == null) {
                    return true;
                }

                display(Math.abs(Integer.parseInt(args[1])), skill, sender, command);
                return true;

            default:
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1:
                return StringUtil.copyPartialMatches(args[0], SkillType.SKILL_NAMES(), new ArrayList<String>(SkillType.SKILL_NAMES().size()));
            default:
                return ImmutableList.of();
        }
    }

    private void display(int page, SkillType skill, CommandSender sender, Command command) {
        if (skill != null && !Permissions.mctop(sender, skill)) {
            sender.sendMessage(command.getPermissionMessage());
            return;
        }

        if (sender instanceof Player) {
            if (!CommandUtils.hasPlayerDataKey(sender)) {
                return;
            }

            McMMOPlayer mcMMOPlayer = UserManager.getPlayer(sender.getName());
            long cooldownMillis = Math.max(Config.getInstance().getDatabasePlayerCooldown(), 1750);

            if (mcMMOPlayer.getDatabaseATS() + cooldownMillis > System.currentTimeMillis()) {
                sender.sendMessage(LocaleLoader.getString("Commands.Database.Cooldown"));
                return;
            }

            if (((Player) sender).hasMetadata(mcMMO.databaseCommandKey)) {
                sender.sendMessage(LocaleLoader.getString("Commands.Database.Processing"));
                return;
            } else {
                ((Player) sender).setMetadata(mcMMO.databaseCommandKey, new FixedMetadataValue(FactionsCaribe.getInstance(), null));
            }

            mcMMOPlayer.actualizeDatabaseATS();
        }

        display(page, skill, sender);
    }

    private void display(int page, SkillType skill, CommandSender sender) {
        boolean useBoard = (sender instanceof Player) && (Config.getInstance().getTopUseBoard());
        boolean useChat = !useBoard || Config.getInstance().getTopUseChat();

        new MctopCommandAsyncTask(page, skill, sender, useBoard, useChat).runTaskAsynchronously(FactionsCaribe.getInstance());
    }

    private SkillType extractSkill(CommandSender sender, String skillName) {
        if (CommandUtils.isInvalidSkill(sender, skillName)) {
            return null;
        }

        SkillType skill = SkillType.getSkill(skillName);

        if (CommandUtils.isChildSkill(sender, skill)) {
            return null;
        }

        return skill;
    }
}
