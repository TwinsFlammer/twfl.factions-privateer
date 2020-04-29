package com.redefocus.factionscaribe.manager;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.registry.CommandRegistry;
import com.redefocus.common.shared.Common;
import com.redefocus.common.shared.databases.mysql.dao.Table;
import com.redefocus.common.shared.databases.redis.channel.data.Channel;
import com.redefocus.common.shared.databases.redis.handler.JedisMessageListener;
import com.redefocus.common.shared.util.ClassGetter;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.mcmmo.config.*;
import com.redefocus.factionscaribe.mcmmo.config.experience.ExperienceConfig;
import com.redefocus.factionscaribe.mcmmo.config.mods.*;
import com.redefocus.factionscaribe.mcmmo.config.party.ItemWeightConfig;
import com.redefocus.factionscaribe.mcmmo.config.skills.alchemy.PotionConfig;
import com.redefocus.factionscaribe.mcmmo.config.skills.repair.RepairConfig;
import com.redefocus.factionscaribe.mcmmo.config.skills.repair.RepairConfigManager;
import com.redefocus.factionscaribe.mcmmo.config.skills.salvage.SalvageConfig;
import com.redefocus.factionscaribe.mcmmo.config.skills.salvage.SalvageConfigManager;
import com.redefocus.factionscaribe.mcmmo.config.treasure.TreasureConfig;
import com.redefocus.factionscaribe.mcmmo.listeners.*;
import com.redefocus.factionscaribe.mcmmo.runnables.party.PartyAutoKickTask;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * @author SrGutyerrez
 */
public class StartManager {
    public static Class<?>[] BLACK_LISTED = {
            BlockListener.class,
            PlayerListener.class,
            EntityListener.class,
            InventoryListener.class,
            SelfListener.class,
            WorldListener.class,
            ConfigLoader.class,
            AutoUpdateConfigLoader.class,
            Config.class,
            PartyAutoKickTask.class,
            RepairConfig.class,
            RepairConfigManager.class,
            PotionConfig.class,
            SalvageConfig.class,
            SalvageConfigManager.class,
            TreasureConfig.class,
            AdvancedConfig.class,
            HiddenConfig.class,
            ItemWeightConfig.class,
            ExperienceConfig.class,
            ArmorConfigManager.class,
            BlockConfigManager.class,
            CustomArmorConfig.class,
            CustomBlockConfig.class,
            CustomEntityConfig.class,
            CustomToolConfig.class,
            EntityConfigManager.class,
            ToolConfigManager.class
    };

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
        ClassGetter.getClassesForPackage(FactionsCaribe.class, StartManager.BLACK_LISTED).forEach(clazz -> {
            if (Listener.class.isAssignableFrom(clazz)) {
                try {
                    Listener listener = (Listener) clazz.newInstance();

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
        ClassGetter.getClassesForPackage(FactionsCaribe.class, StartManager.BLACK_LISTED).forEach(clazz -> {
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
        ClassGetter.getClassesForPackage(FactionsCaribe.class, StartManager.BLACK_LISTED).forEach(clazz -> {
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
        ClassGetter.getClassesForPackage(FactionsCaribe.class, StartManager.BLACK_LISTED).forEach(clazz -> {
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
        ClassGetter.getClassesForPackage(FactionsCaribe.class, StartManager.BLACK_LISTED).forEach(clazz -> {
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

class DataManager {
    DataManager() {

    }
}