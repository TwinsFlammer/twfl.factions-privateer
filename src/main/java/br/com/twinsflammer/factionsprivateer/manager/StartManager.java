package br.com.twinsflammer.factionsprivateer.manager;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.registry.CommandRegistry;
import br.com.twinsflammer.common.shared.Common;
import br.com.twinsflammer.common.shared.databases.mysql.dao.Table;
import br.com.twinsflammer.common.shared.databases.redis.channel.data.Channel;
import br.com.twinsflammer.common.shared.databases.redis.handler.JedisMessageListener;
import br.com.twinsflammer.common.shared.util.ClassGetter;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.combat.runnable.CombatSendActionBarRunnable;
import br.com.twinsflammer.factionsprivateer.commands.player.tpa.runnable.TpaRequestRunnable;
import br.com.twinsflammer.factionsprivateer.economy.manager.EconomyManager;
import br.com.twinsflammer.factionsprivateer.specialitem.data.AbstractSpecialItem;
import br.com.twinsflammer.factionsprivateer.specialitem.manager.SpecialItemManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;

/**
 * @author SrGutyerrez
 */
public class StartManager {
    protected final static String[] BLACKLISTED_PACKAGES = {
            "br.com.twinsflammer.factionsprivateer.mcmmo"
    };

    public StartManager() {
        new ListenerManager();

        new CommandManager();

        new TableManager();

        new DataManager();

        new ChannelManager();

        new JedisMessageListenerManager();

        new AbstractSpecialItemManager();

        new RunnableManager();
    }
}

class ListenerManager {
    ListenerManager() {
        ClassGetter.getClassesForPackage(FactionsPrivateer.class, StartManager.BLACKLISTED_PACKAGES).forEach(clazz -> {
            if (Listener.class.isAssignableFrom(clazz)) {
                try {
                    Listener listener = (Listener) clazz.newInstance();

                    System.out.println("[Listener] Registering class " + clazz.getName());

                    Bukkit.getPluginManager().registerEvents(
                            listener,
                            FactionsPrivateer.getInstance()
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
        ClassGetter.getClassesForPackage(FactionsPrivateer.class, StartManager.BLACKLISTED_PACKAGES).forEach(clazz -> {
            if (CustomCommand.class.isAssignableFrom(clazz)) {
                try {
                    CustomCommand customCommand = (CustomCommand) clazz.newInstance();

                    CommandRegistry.registerCommand(
                            FactionsPrivateer.getInstance(),
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
        ClassGetter.getClassesForPackage(FactionsPrivateer.class, StartManager.BLACKLISTED_PACKAGES).forEach(clazz -> {
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
        ClassGetter.getClassesForPackage(FactionsPrivateer.class, StartManager.BLACKLISTED_PACKAGES).forEach(clazz -> {
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
        ClassGetter.getClassesForPackage(FactionsPrivateer.class, StartManager.BLACKLISTED_PACKAGES).forEach(clazz -> {
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
        ClassGetter.getClassesForPackage(FactionsPrivateer.class, StartManager.BLACKLISTED_PACKAGES).forEach(clazz -> {
            if (!clazz.equals(AbstractSpecialItem.class) && AbstractSpecialItem.class.isAssignableFrom(clazz)) {
                try {
                    AbstractSpecialItem abstractSpecialItem = (AbstractSpecialItem) clazz.newInstance();

                    SpecialItemManager.registerSpecialItem(
                            abstractSpecialItem,
                            FactionsPrivateer.getInstance()
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