package br.com.twinsflammer.factionsprivateer.commands.player.tpa.channel;

import br.com.twinsflammer.common.shared.databases.redis.channel.data.Channel;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * @author SrGutyerrez
 */
public class TpaChannel extends Channel {
    public static final String CHANNEL_NAME = "tpa_channel";

    @Override
    public String getName() {
        return TpaChannel.CHANNEL_NAME;
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
