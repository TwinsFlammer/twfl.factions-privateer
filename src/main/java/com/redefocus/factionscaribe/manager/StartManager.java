package com.redefocus.factionscaribe.manager;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.registry.CommandRegistry;
import com.redefocus.common.shared.Common;
import com.redefocus.common.shared.databases.mysql.dao.Table;
import com.redefocus.common.shared.databases.redis.channel.data.Channel;
import com.redefocus.common.shared.databases.redis.handler.JedisMessageListener;
import com.redefocus.common.shared.util.ClassGetter;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.combat.runnable.CombatSendActionBarRunnable;
import com.redefocus.factionscaribe.commands.player.tpa.runnable.TpaRequestRunnable;
import com.redefocus.factionscaribe.economy.manager.EconomyManager;
import com.redefocus.factionscaribe.specialitem.data.AbstractSpecialItem;
import com.redefocus.factionscaribe.specialitem.manager.SpecialItemManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;

/**
 * @author SrGutyerrez
 */
public class StartManager {
    public StartManager() {
//        new ListenerManager();

        new CommandManager();

        new TableManager();

        new DataManager();

        new ChannelManager();

        new JedisMessageListenerManager();

        new AbstractSpecialItemManager();

//        new RunnableManager();
    }
}

class ListenerManager {
    ListenerManager() {
        ClassGetter.getClassesForPackage(FactionsCaribe.class).forEach(clazz -> {
            if (Listener.class.isAssignableFrom(clazz)) {
                try {
                    Listener listener = (Listener) clazz.newInstance();

                    System.out.println("[Listener] Registering class " + clazz.getName());

                    Bukkit.getPluginManager().registerEvents(
                            listener,
                            FactionsCaribe.getInstance()
                    );
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class CommandManager {
    CommandManager() {
        ClassGetter.getClassesForPackage(FactionsCaribe.class).forEach(clazz -> {
            if (CustomCommand.class.isAssignableFrom(clazz)) {
                try {
                    CustomCommand customCommand = (CustomCommand) clazz.newInstance();

                    CommandRegistry.registerCommand(
                            FactionsCaribe.getInstance(),
                            customCommand
                    );
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class TableManager {
    TableManager() {
        ClassGetter.getClassesForPackage(FactionsCaribe.class).forEach(clazz -> {
            if (Table.class.isAssignableFrom(clazz)) {
                try {
                    Table table = (Table) clazz.newInstance();

                    table.createTable();
                } catch (IllegalAccessException | InstantiationException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class ChannelManager {
    ChannelManager() {
        ClassGetter.getClassesForPackage(FactionsCaribe.class).forEach(clazz -> {
            if (Channel.class.isAssignableFrom(clazz)) {
                try {
                    Channel channel = (Channel) clazz.newInstance();

                    Common.getInstance().getChannelManager().register(channel);
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class JedisMessageListenerManager {
    JedisMessageListenerManager() {
        ClassGetter.getClassesForPackage(FactionsCaribe.class).forEach(clazz -> {
            if (JedisMessageListener.class.isAssignableFrom(clazz)) {
                try {
                    JedisMessageListener jedisMessageListener = (JedisMessageListener) clazz.newInstance();

                    Common.getInstance().getJedisMessageManager().registerListener(jedisMessageListener);
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class AbstractSpecialItemManager {
    AbstractSpecialItemManager() {
        ClassGetter.getClassesForPackage(FactionsCaribe.class, AbstractSpecialItem.class).forEach(clazz -> {
            if (AbstractSpecialItem.class.isAssignableFrom(clazz)) {
                try {
                    AbstractSpecialItem abstractSpecialItem = (AbstractSpecialItem) clazz.newInstance();

                    SpecialItemManager.registerSpecialItem(
                            abstractSpecialItem,
                            FactionsCaribe.getInstance()
                    );
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class DataManager {
    DataManager() {
        new EconomyManager();
        new SpecialItemManager();
    }
}

class RunnableManager {
    RunnableManager() {
        Common.getInstance().getScheduler().scheduleAtFixedRate(
                new CombatSendActionBarRunnable(),
                0,
                500,
                TimeUnit.MILLISECONDS
        );
        Common.getInstance().getScheduler().scheduleAtFixedRate(
                new TpaRequestRunnable(),
                0,
                1,
                TimeUnit.SECONDS
        );
    }
}