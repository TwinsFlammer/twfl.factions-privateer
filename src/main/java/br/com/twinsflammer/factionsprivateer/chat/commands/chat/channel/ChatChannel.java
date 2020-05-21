package br.com.twinsflammer.factionsprivateer.chat.commands.chat.channel;

import br.com.twinsflammer.common.shared.databases.redis.channel.data.Channel;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * @author SrGutyerrez
 */
public class ChatChannel extends Channel {
    public static final String CHANNEL_NAME = "chat_channel";

    @Override
    public String getName() {
        return ChatChannel.CHANNEL_NAME;
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
