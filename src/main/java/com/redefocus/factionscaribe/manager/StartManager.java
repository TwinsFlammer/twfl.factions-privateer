package com.redefocus.factionscaribe.manager;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.registry.CommandRegistry;
import com.redefocus.common.shared.databases.mysql.dao.Table;
import com.redefocus.common.shared.util.ClassGetter;
import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * @author SrGutyerrez
 */
public class StartManager {
    public StartManager() {
        new ListenerManager();

        new CommandManager();

        new TableManager();

        new DataManager();
    }
}

class ListenerManager {
    ListenerManager() {
        ClassGetter.getClassesForPackage(FactionsCaribe.class).forEach(clazz -> {
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

class DataManager {
    DataManager() {

    }
}