package br.com.twinsflammer.factionsprivateer.mcmmo.chat;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.events.chat.McMMOAdminChatEvent;
import org.bukkit.plugin.Plugin;

public class AdminChatManager extends ChatManager {

    protected AdminChatManager(Plugin plugin) {
        super(plugin, Config.getInstance().getAdminDisplayNames(), Config.getInstance().getAdminChatPrefix());
    }

    @Override
    public void handleChat(String senderName, String displayName, String message, boolean isAsync) {
        handleChat(new McMMOAdminChatEvent(plugin, senderName, displayName, message, isAsync));
    }

    @Override
    protected void sendMessage() {
        plugin.getServer().broadcast(message, "mcmmo.chat.adminchat");
    }
}
