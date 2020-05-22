package br.com.twinsflammer.factionsprivateer.commands.player.tpa.listener;

import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.teleport.data.TeleportRequest;
import br.com.twinsflammer.common.shared.databases.redis.handler.JedisMessageListener;
import br.com.twinsflammer.common.shared.databases.redis.handler.annonation.ChannelName;
import br.com.twinsflammer.common.shared.databases.redis.handler.event.JedisMessageEvent;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.commands.player.tpa.channel.TpaChannel;
import br.com.twinsflammer.factionsprivateer.commands.player.tpa.data.TpaRequest;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
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

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(userId),
                privateerUser1 = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(targetId);

        Server currentServer = SpigotAPI.getCurrentServer();

        if (action == TpaRequest.Action.SEND) {
            if (privateerUser.getServerId().equals(currentServer.getId()))
                privateerUser.getTeleportRequestsSent().add(tpaRequest);

            if (privateerUser1.getServerId().equals(currentServer.getId()))
                privateerUser1.getTeleportRequestsReceived().add(tpaRequest);
        } else if (action == TpaRequest.Action.ACCEPT) {
            if (!privateerUser.getServerId().equals(currentServer.getId())) return;

            TeleportRequest teleportRequest = new TeleportRequest(
                    privateerUser.getId(),
                    privateerUser1.getId(),
                    null,
                    privateerUser1.getServerId(),
                    privateerUser.getTeleportTime()
            );

            teleportRequest.start();

            privateerUser.removeTpaRequest(tpaRequest);
            privateerUser1.removeTpaRequest(tpaRequest);

            privateerUser.setLastTpaTime(System.currentTimeMillis());
        } else {
            privateerUser.removeTpaRequest(tpaRequest);
            privateerUser1.removeTpaRequest(tpaRequest);
        }
    }
}
