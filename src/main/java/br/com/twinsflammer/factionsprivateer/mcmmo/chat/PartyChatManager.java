package br.com.twinsflammer.factionsprivateer.mcmmo.chat;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.chat.McMMOPartyChatEvent;
import br.com.twinsflammer.factionsprivateer.mcmmo.runnables.party.PartyChatTask;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.Party;
import org.bukkit.plugin.Plugin;

public class PartyChatManager extends ChatManager {

    private Party party;

    protected PartyChatManager(Plugin plugin) {
        super(plugin, Config.getInstance().getPartyDisplayNames(), Config.getInstance().getPartyChatPrefix());
    }

    public void setParty(Party party) {
        this.party = party;
    }

    @Override
    public void handleChat(String senderName, String displayName, String message, boolean isAsync) {
        handleChat(new McMMOPartyChatEvent(plugin, senderName, displayName, party.getName(), message, isAsync));
    }

    @Override
    protected void sendMessage() {
        new PartyChatTask(plugin, party, senderName, displayName, message).runTask(plugin);
    }
}
