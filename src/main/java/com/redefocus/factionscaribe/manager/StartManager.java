package com.redefocus.factionscaribe.manager;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.registry.CommandRegistry;
import com.redefocus.common.shared.Common;
import com.redefocus.common.shared.databases.mysql.dao.Table;
import com.redefocus.common.shared.databases.redis.channel.data.Channel;
import com.redefocus.common.shared.databases.redis.handler.JedisMessageListener;
import com.redefocus.common.shared.util.ClassGetter;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.mcmmo.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Arrays;

/**
 * @author SrGutyerrez
 */
public class StartManager {
    public StartManager() {
        new ListenerManager();

        new CommandManager();

        new TableManager();

        new DataManager();

        new ChannelManager();

        new JedisMessageListenerManager();
    }
}

class ListenerManager {
    ListenerManager() {
        Class<?>[] blacklisted = {
                BlockListener.class,
                PlayerListener.class,
                EntityListener.class,
                InventoryListener.class,
                SelfListener.class,
                WorldListener.class
        };
        
//        ClassGetter.getClassesForPackage(FactionsCaribe.class).forEach(clazz -> {
//            System.out.println(clazz);
//
//            if (!Arrays.asList(blacklisted).contains(clazz)) {
//                System.out.println("Registrando: " + clazz);
//
//                if (Listener.class.isAssignableFrom(clazz)) {
//                    try {
//                        Listener listener = (Listener) clazz.newInstance();
//
//                        Bukkit.getPluginManager().registerEvents(
//                                listener,
//                                FactionsCaribe.getInstance()
//                        );
//                    } catch (InstantiationException | IllegalAccessException exception) {
//                        exception.printStackTrace();
//                    }
//                }
//            } else {
//                System.out.println("Contém: " + clazz);
//            }
//        });
    }
}

class CommandManager {
    CommandManager() {
//        ClassGetter.getClassesForPackage(FactionsCaribe.class).forEach(clazz -> {
//            if (CustomCommand.class.isAssignableFrom(clazz)) {
//                try {
//                    CustomCommand customCommand = (CustomCommand) clazz.newInstance();
//
//                    CommandRegistry.registerCommand(
//                            FactionsCaribe.getInstance(),
//                            customCommand
//                    );
//                } catch (InstantiationException | IllegalAccessException exception) {
//                    exception.printStackTrace();
//                }
//            }
//        });
    }
}

class TableManager {
    TableManager() {
//        ClassGetter.getClassesForPackage(FactionsCaribe.class).forEach(clazz -> {
//            if (Table.class.isAssignableFrom(clazz)) {
//                try {
//                    Table table = (Table) clazz.newInstance();
//
//                    table.createTable();
//                } catch (IllegalAccessException | InstantiationException exception) {
//                    exception.printStackTrace();
//                }
//            }
//        });
    }
}

class ChannelManager {
    ChannelManager() {
//        ClassGetter.getClassesForPackage(FactionsCaribe.class).forEach(clazz -> {
//            if (Channel.class.isAssignableFrom(clazz)) {
//                try {
//                    Channel channel = (Channel) clazz.newInstance();
//
//                    Common.getInstance().getChannelManager().register(channel);
//                } catch (InstantiationException | IllegalAccessException exception) {
//                    exception.printStackTrace();
//                }
//            }
//        });
    }
}

class JedisMessageListenerManager {
    JedisMessageListenerManager() {
//        ClassGetter.getClassesForPackage(FactionsCaribe.class).forEach(clazz -> {
//            if (JedisMessageListener.class.isAssignableFrom(clazz)) {
//                try {
//                    JedisMessageListener jedisMessageListener = (JedisMessageListener) clazz.newInstance();
//
//                    Common.getInstance().getJedisMessageManager().registerListener(jedisMessageListener);
//                } catch (InstantiationException | IllegalAccessException exception) {
//                    exception.printStackTrace();
//                }
//            }
//        });
    }
}

class DataManager {
    DataManager() {

    }
}