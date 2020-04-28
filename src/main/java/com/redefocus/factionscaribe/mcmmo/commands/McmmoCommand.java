package com.redefocus.factionscaribe.mcmmo.commands;

import com.redefocus.factionscaribe.mcmmo.commands.party.PartySubcommandType;
import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.mcMMO;
import com.redefocus.factionscaribe.mcmmo.util.Permissions;
import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class McmmoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 0:
                if (!Permissions.mcmmoDescription(sender)) {
                    sender.sendMessage(command.getPermissionMessage());
                    return true;
                }

                String description = LocaleLoader.getString("mcMMO.Description");
                String[] mcSplit = description.split(",");
                sender.sendMessage(mcSplit);

                if (Config.getInstance().getDonateMessageEnabled()) {
                    sender.sendMessage(LocaleLoader.getString("MOTD.Donate"));
                    sender.sendMessage(ChatColor.GOLD + " - " + ChatColor.GREEN + "tft_02@hotmail.com" + ChatColor.GOLD + " Paypal");
                }

                if (Permissions.showversion(sender)) {
                    sender.sendMessage(LocaleLoader.getString("MOTD.Version", FactionsCaribe.getInstance().getDescription().getVersion()));
                }

                mcMMO.getHolidayManager().anniversaryCheck(sender);
                return true;

            case 1:
                if (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("commands")) {
                    if (!Permissions.mcmmoHelp(sender)) {
                        sender.sendMessage(command.getPermissionMessage());
                        return true;
                    }

                    sender.sendMessage(LocaleLoader.getString("Commands.mcc.Header"));
                    displayGeneralCommands(sender);
                    displayOtherCommands(sender);
                    displayPartyCommands(sender);
                }
                return true;

            default:
                return false;
        }
    }

    private void displayGeneralCommands(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_AQUA + " /mcstats " + LocaleLoader.getString("Commands.Stats"));
        sender.sendMessage(ChatColor.DARK_AQUA + " /<skill>" + LocaleLoader.getString("Commands.SkillInfo"));
        sender.sendMessage(ChatColor.DARK_AQUA + " /mctop " + LocaleLoader.getString("Commands.Leaderboards"));

        if (Permissions.inspect(sender)) {
            sender.sendMessage(ChatColor.DARK_AQUA + " /inspect " + LocaleLoader.getString("Commands.Inspect"));
        }

        if (Permissions.mcability(sender)) {
            sender.sendMessage(ChatColor.DARK_AQUA + " /mcability " + LocaleLoader.getString("Commands.ToggleAbility"));
        }
    }

    private void displayOtherCommands(CommandSender sender) {
        sender.sendMessage(LocaleLoader.getString("Commands.Other"));

        if (Permissions.skillreset(sender)) {
            sender.sendMessage(ChatColor.DARK_AQUA + " /skillreset <skill|all> " + LocaleLoader.getString("Commands.Reset"));
        }

        if (Permissions.mmoedit(sender)) {
            sender.sendMessage(ChatColor.DARK_AQUA + " /mmoedit " + LocaleLoader.getString("Commands.mmoedit"));
        }

        if (Permissions.adminChat(sender)) {
            sender.sendMessage(ChatColor.DARK_AQUA + " /adminchat " + LocaleLoader.getString("Commands.AdminToggle"));
        }

        if (Permissions.mcgod(sender)) {
            sender.sendMessage(ChatColor.DARK_AQUA + " /mcgod " + LocaleLoader.getString("Commands.mcgod"));
        }
    }

    private void displayPartyCommands(CommandSender sender) {
        if (Permissions.party(sender)) {
            sender.sendMessage(LocaleLoader.getString("Commands.Party.Commands"));
            sender.sendMessage(ChatColor.DARK_AQUA + " /party create <" + LocaleLoader.getString("Commands.Usage.PartyName") + "> " + LocaleLoader.getString("Commands.Party1"));
            sender.sendMessage(ChatColor.DARK_AQUA + " /party join <" + LocaleLoader.getString("Commands.Usage.Player") + "> " + LocaleLoader.getString("Commands.Party2"));
            sender.sendMessage(ChatColor.DARK_AQUA + " /party quit " + LocaleLoader.getString("Commands.Party.Quit"));

            if (Permissions.partyChat(sender)) {
                sender.sendMessage(ChatColor.DARK_AQUA + " /party chat " + LocaleLoader.getString("Commands.Party.Toggle"));
            }

            sender.sendMessage(ChatColor.DARK_AQUA + " /party invite <" + LocaleLoader.getString("Commands.Usage.Player") + "> " + LocaleLoader.getString("Commands.Party.Invite"));
            sender.sendMessage(ChatColor.DARK_AQUA + " /party accept " + LocaleLoader.getString("Commands.Party.Accept"));

            if (Permissions.partySubcommand(sender, PartySubcommandType.TELEPORT)) {
                sender.sendMessage(ChatColor.DARK_AQUA + " /party teleport <" + LocaleLoader.getString("Commands.Usage.Player") + "> " + LocaleLoader.getString("Commands.Party.Teleport"));
            }
        }
    }
}
