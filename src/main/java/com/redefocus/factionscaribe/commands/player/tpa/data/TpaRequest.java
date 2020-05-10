package com.redefocus.factionscaribe.commands.player.tpa.data;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.factionscaribe.commands.player.tpa.channel.TpaChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.UUID;

/**
 * @author SrGutyerrez
 */
@AllArgsConstructor
public class TpaRequest {
    @Getter
    private final UUID uuid;
    @Getter
    private final Integer userId, targetId, rootServerId;

    @Getter
    @Setter
    private Action action;

    @Getter
    private final Long expireTime;

    public void publish() {
        TpaChannel tpaChannel = new TpaChannel();

        tpaChannel.sendMessage(this.toString());
    }

    public Boolean hasExpired() {
        System.out.println(this.expireTime);
        System.out.println(System.currentTimeMillis());

        return this.expireTime <= System.currentTimeMillis();
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("uuid", this.uuid.toString());
        jsonObject.put("user_id", this.userId);
        jsonObject.put("target_id", this.targetId);
        jsonObject.put("root_server_id", SpigotAPI.getRootServerId());
        jsonObject.put("action", this.action.toString());
        jsonObject.put("expire_time", System.currentTimeMillis());

        return jsonObject.toString();
    }

    public static enum Action {
        SEND,
        CANCEL,
        ACCEPT,
        DENY;
    }
}
