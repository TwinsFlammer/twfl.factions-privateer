package com.redefocus.factionscaribe.user.item.listener;

import com.redefocus.api.spigot.util.serialize.ItemSerialize;
import com.redefocus.common.shared.databases.redis.handler.JedisMessageListener;
import com.redefocus.common.shared.databases.redis.handler.annonation.ChannelName;
import com.redefocus.common.shared.databases.redis.handler.event.JedisMessageEvent;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import com.redefocus.factionscaribe.user.item.channel.ItemChannel;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author SrGutyerrez
 */
public class ItemJedisMessageListener implements JedisMessageListener {
    @ChannelName(name = ItemChannel.CHANNEL_NAME)
    public void onMessage(JedisMessageEvent event) {
        String message = event.getMessage();

        JSONObject jsonObject = (JSONObject) JSONValue.parse(message);

        Integer userId = ((Long) jsonObject.get("user_id")).intValue();
        String serializedItem = (String) jsonObject.get("item");

        ItemStack itemStack = ItemSerialize.fromBase64(serializedItem);

        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(userId);

        Player player = caribeUser.getPlayer();

        if (player == null) return;

        player.getInventory().addItem(itemStack);
    }
}
