package br.com.twinsflammer.factionscaribe.mcmmo.commands.party.alliance;

import br.com.twinsflammer.factionscaribe.mcmmo.config.Config;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.party.Party;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.party.PartyFeature;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.party.PartyManager;
import br.com.twinsflammer.factionscaribe.mcmmo.util.commands.CommandUtils;
import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class PartyAllianceCommand implements TabExecutor {

    private Player player;
    private Party playerParty;
    private Party targetParty;

    public static final List<String> ALLIANCE_SUBCOMMANDS = ImmutableList.of("invite", "accept", "disband");

    private CommandExecutor partyAllianceInviteCommand = new PartyAllianceInviteCommand();
    private CommandExecutor partyAllianceAcceptCommand = new PartyAllianceAcceptCommand();
    private CommandExecutor partyAllianceDisbandCommand = new PartyAllianceDisbandCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (CommandUtils.noConsoleUsage(sender)) {
            return true;
        }

        player = (Player) sender;
        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);

        playerParty = mcMMOPlayer.getParty();

        switch (args.length) {
            case 1:
                if (playerParty.getLevel() < Config.getInstance().getPartyFeatureUnlockLevel(PartyFeature.ALLIANCE)) {
                    sender.sendMessage(LocaleLoader.getString("Party.Feature.Disabled.3"));
                    return true;
                }

                if (playerParty.getAlly() == null) {
                    printUsage();
                    return true;
                }

                targetParty = playerParty.getAlly();

                displayPartyHeader();
                displayMemberInfo(mcMMOPlayer);
                return true;

            case 2:
            case 3:
                if (playerParty.getLevel() < Config.getInstance().getPartyFeatureUnlockLevel(PartyFeature.ALLIANCE)) {
                    sender.sendMessage(LocaleLoader.getString("Party.Feature.Disabled.3"));
                    return true;
                }

                if (args[1].equalsIgnoreCase("invite")) {
                    return partyAllianceInviteCommand.onCommand(sender, command, label, args);
                }

                if (args[1].equalsIgnoreCase("accept")) {
                    return partyAllianceAcceptCommand.onCommand(sender, command, label, args);
                }

                if (args[1].equalsIgnoreCase("disband")) {
                    return partyAllianceDisbandCommand.onCommand(sender, command, label, args);
                }

                if (playerParty.getAlly() == null) {
                    printUsage();
                    return true;
                }

                targetParty = playerParty.getAlly();

                displayPartyHeader();
                displayMemberInfo(mcMMOPlayer);
                return true;

            default:
                return false;
        }
    }

    private boolean printUsage() {
        player.sendMessage(LocaleLoader.getString("Commands.Party.Alliance.Help.0"));
        player.sendMessage(LocaleLoader.getString("Commands.Party.Alliance.Help.1"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                List<String> matches = StringUtil.copyPartialMatches(args[0], ALLIANCE_SUBCOMMANDS, new ArrayList<String>(ALLIANCE_SUBCOMMANDS.size()));

                if (matches.size() == 0) {
                    List<String> playerNames = CommandUtils.getOnlinePlayerNames(commandSender);
                    return StringUtil.copyPartialMatches(args[0], playerNames, new ArrayList<String>(playerNames.size()));
                }

                return matches;
            default:
                return ImmutableList.of();
        }
    }

    private void displayPartyHeader() {
        player.sendMessage(LocaleLoader.getString("Commands.Party.Alliance.Header"));
        player.sendMessage(LocaleLoader.getString("Commands.Party.Alliance.Ally", playerParty.getName(), targetParty.getName()));
    }

    private void displayMemberInfo(McMMOPlayer mcMMOPlayer) {
        List<Player> nearMembers = PartyManager.getNearMembers(mcMMOPlayer);
        player.sendMessage(LocaleLoader.getString("Commands.Party.Alliance.Members.Header"));
        player.sendMessage(playerParty.createMembersList(player.getName(), nearMembers));
        player.sendMessage(ChatColor.DARK_GRAY + "----------------------------");
        player.sendMessage(targetParty.createMembersList(player.getName(), nearMembers));
    }
}
