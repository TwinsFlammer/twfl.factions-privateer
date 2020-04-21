package com.redefocus.factionscaribe.commands.staff.clear.listener;

import com.redefocus.common.shared.databases.redis.handler.JedisMessageListener;
import com.redefocus.common.shared.databases.redis.handler.annonation.ChannelName;
import com.redefocus.common.shared.databases.redis.handler.event.JedisMessageEvent;
import com.redefocus.common.shared.server.data.Server;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
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
