package br.com.twinsflammer.factionscaribe.commands.staff.clear.listener;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import br.com.twinsflammer.common.shared.databases.redis.handler.JedisMessageListener;
import br.com.twinsflammer.common.shared.databases.redis.handler.annonation.ChannelName;
import br.com.twinsflammer.common.shared.databases.redis.handler.event.JedisMessageEvent;
import br.com.twinsflammer.common.shared.server.data.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author SrGutyerrez
 */
public class ClearJedisMessageListener implements JedisMessageListener {
    @ChannelName(name = "clear_channel")
    public void onMessage(JedisMessageEvent event) {
        String message = event.getMessage();

        JSONObject jsonObject = (JSONObject) JSONValue.parse(message);

        Integer userId = ((Long) jsonObject.get("user_id")).intValue();

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(userId);

        Server server = caribeUser.getServer();

        if (server.isLoginServer() || server.isLobby()) return;

        Player player = caribeUser.getPlayer();

        if (player != null) {
            PlayerInventory playerInventory = player.getInventory();

            playerInventory.clear();
            playerInventory.setArmorContents(null);
        }
    }
}
