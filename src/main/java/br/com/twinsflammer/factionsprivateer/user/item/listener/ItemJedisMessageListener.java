package br.com.twinsflammer.factionsprivateer.user.item.listener;

import br.com.twinsflammer.api.spigot.util.serialize.ItemSerialize;
import br.com.twinsflammer.common.shared.databases.redis.handler.JedisMessageListener;
import br.com.twinsflammer.common.shared.databases.redis.handler.annonation.ChannelName;
import br.com.twinsflammer.common.shared.databases.redis.handler.event.JedisMessageEvent;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.factionsprivateer.user.item.channel.ItemChannel;
import br.com.twinsflammer.factionsprivateer.user.item.event.PlayerItemReceiveEvent;
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

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(userId);

        Player player = privateerUser.getPlayer();

        if (player == null) return;

        PlayerItemReceiveEvent playerItemReceiveEvent = new PlayerItemReceiveEvent(
                player,
                itemStack
        );

        playerItemReceiveEvent.run();

        player.getInventory().addItem(itemStack);
    }
}
