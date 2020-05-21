package br.com.twinsflammer.factionsprivateer.user.item.channel;

import br.com.twinsflammer.common.shared.databases.redis.channel.data.Channel;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * @author SrGutyerrez
 */
public class ItemChannel extends Channel {
    public static final String CHANNEL_NAME = "item_channel";

    @Override
    public String getName() {
        return ItemChannel.CHANNEL_NAME;
    }

    @Override
    public void sendMessage(String message) {
        try (Jedis jedis = this.getJedisPool().getResource()) {
            jedis.publish(
                    this.getName(),
                    message
            );
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }
    }
}
