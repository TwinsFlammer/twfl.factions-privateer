package com.redefocus.factionscaribe.mcmmo.commands.chat;

import com.redefocus.factionscaribe.mcmmo.chat.PartyChatManager;
import com.redefocus.factionscaribe.mcmmo.config.Config;
import com.redefocus.factionscaribe.mcmmo.datatypes.chat.ChatMode;
import com.redefocus.factionscaribe.mcmmo.datatypes.party.Party;
import com.redefocus.factionscaribe.mcmmo.datatypes.party.PartyFeature;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.party.PartyManager;
import com.redefocus.factionscaribe.mcmmo.util.player.UserManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyChatCommand extends ChatCommand {

    public PartyChatCommand() {
        super(ChatMode.PARTY);
    }

    @Override
    protected void handleChatSending(CommandSender sender, String[] args) {
        Party party;
        String message;

        if (sender instanceof Player) {
            party = UserManager.getPlayer((Player) sender).getParty();

            if (party == null) {
                sender.sendMessage(LocaleLoader.getString("Commands.Party.None"));
                return;
            }

            if (party.getLevel() < Config.getInstance().getPartyFeatureUnlockLevel(PartyFeature.CHAT)) {
                sender.sendMessage(LocaleLoader.getString("Party.Feature.Disabled.1"));
                return;
            }

            message = buildChatMessage(args, 0);
        } else {
            if (args.length < 2) {
                sender.sendMessage(LocaleLoader.getString("Party.Specify"));
                return;
            }

            party = PartyManager.getParty(args[0]);

            if (party == null) {
                sender.sendMessage(LocaleLoader.getString("Party.InvalidName"));
                return;
            }

            message = buildChatMessage(args, 1);
        }

        ((PartyChatManager) chatManager).setParty(party);
        chatManager.handleChat(sender.getName(), getDisplayName(sender), message);
    }
}
