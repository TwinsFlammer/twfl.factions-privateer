package com.redefocus.factionscaribe.chat.commands.chat.factory;

import com.redefocus.common.shared.databases.redis.manager.RedisManager;
import com.redefocus.factionscaribe.chat.enums.Channel;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * @author SrGutyerrez
 */
public class ChatFactory<C extends Channel> {
    public Boolean isChannelActive(C channel) {
        try (
                Jedis jedis = RedisManager.getDefaultRedis().getJedisPool().getResource()
        ) {
            return !jedis.exists(channel.getHKey());
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public void changeChannelStatus(C channel, Boolean action) {
        try (
                Jedis jedis = RedisManager.getDefaultRedis().getJedisPool().getResource()
        ) {
            if (action)
                jedis.hdel(channel.getHKey());
            else
                jedis.hset(channel.getHKey(), "channel", channel.name());
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }
    }
}
