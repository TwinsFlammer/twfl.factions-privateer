package com.redefocus.factionscaribe.commands.player.tpa.listener;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.api.spigot.teleport.data.TeleportRequest;
import com.redefocus.common.shared.databases.redis.handler.JedisMessageListener;
import com.redefocus.common.shared.databases.redis.handler.annonation.ChannelName;
import com.redefocus.common.shared.databases.redis.handler.event.JedisMessageEvent;
import com.redefocus.common.shared.server.data.Server;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.commands.player.tpa.channel.TpaChannel;
import com.redefocus.factionscaribe.commands.player.tpa.data.TpaRequest;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.UUID;

/**
 * @author SrGutyerrez
 */
public class TpaChannelJedisMessageListener implements JedisMessageListener {
    @ChannelName(name = TpaChannel.CHANNEL_NAME)
    public void onMessage(JedisMessageEvent event) {
        String message = event.getMessage();

        JSONObject jsonObject = (JSONObject) JSONValue.parse(message);

        String preUUID = (String) jsonObject.get("uuid");

        UUID uuid = UUID.fromString(preUUID);

        Integer userId = ((Long) jsonObject.get("user_id")).intValue(),
                targetId = ((Long) jsonObject.get("target_id")).intValue(),
                rootServerId = ((Long) jsonObject.get("root_server_id")).intValue();

        if (!SpigotAPI.getRootServerId().equals(rootServerId)) return;

        String preAction = (String) jsonObject.get("action");

        TpaRequest.Action action = TpaRequest.Action.valueOf(preAction);

        Long expireTime = (Long) jsonObject.get("expire_time");

        TpaRequest tpaRequest = new TpaRequest(
                uuid,
                userId,
                targetId,
                rootServerId,
                action,
                expireTime
        );

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(userId),
                caribeUser1 = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(targetId);

        Server currentServer = SpigotAPI.getCurrentServer();

        if (action == TpaRequest.Action.SEND) {
            if (caribeUser.getServerId().equals(currentServer.getId()))
                caribeUser.getTeleportRequestsSent().add(tpaRequest);

            if (caribeUser1.getServerId().equals(currentServer.getId()))
                caribeUser1.getTeleportRequestsReceived().add(tpaRequest);
        } else if (action == TpaRequest.Action.ACCEPT) {
            if (!caribeUser.getServerId().equals(currentServer.getId())) return;

            TeleportRequest teleportRequest = new TeleportRequest(
                    caribeUser.getId(),
                    caribeUser1.getId(),
                    null,
                    caribeUser1.getServerId(),
                    caribeUser.getTeleportTime()
            );

            teleportRequest.start();

            caribeUser.removeTpaRequest(tpaRequest);
            caribeUser1.removeTpaRequest(tpaRequest);
        } else {
            caribeUser.removeTpaRequest(tpaRequest);
            caribeUser1.removeTpaRequest(tpaRequest);
        }
    }
}
