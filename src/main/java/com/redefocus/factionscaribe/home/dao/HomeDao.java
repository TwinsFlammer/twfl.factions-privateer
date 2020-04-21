package com.redefocus.factionscaribe.home.dao;

import com.google.common.collect.Sets;
import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.api.spigot.util.serialize.LocationSerialize;
import com.redefocus.common.shared.databases.mysql.dao.Table;
import com.redefocus.factionscaribe.home.data.Home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

/**
 * @author oNospher
 **/
public class HomeDao<T extends Home> extends Table {

    @Override
    public String getDatabaseName() {
        return "server";
    }

    @Override
    public String getTableName() {
        return "server_home";
    }

    @Override
    public void createTable() {
        this.execute(
                String.format(
                        "CREATE TABLE IF NOT EXISTS `%s` " +
                                "(" +
                                "`id` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                                "`user_id` INTEGER NOT NULL," +
                                "`name` VARCHAR(32) NOT NULL," +
                                "`server_id` INTEGER NOT NULL," +
                                "`location` VARCHAR(255) NOT NULL," +
                                "`state` VARCHAR(7) NOT NULL" +
                                ");",
                        this.getTableName()
                )
        );
    }

    public T insert(T home) {
        String query = String.format(
                "INSERT INTO `%s` " +
                        "(" +
                        "`user_id`," +
                        "`name`," +
                        "`server_id`," +
                        "`location`" +
                        "`state`" +
                        ")" +
                        " VALUES " +
                        "(" +
                        "'%s'" +
                        "'%s'" +
                        "'%s'" +
                        "'%s'" +
                        "'%s'" +
                        ");",
                this.getTableName(),
                home.getUserId(),
                home.getName(),
                home.getServerId(),
                LocationSerialize.toString(home.getLocation()),
                home.getState()
        );
        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            preparedStatement.execute();

            if (resultSet.next()) {
                return (T) new Home(
                        resultSet.getInt("id"),
                        home.getUserId(),
                        home.getName(),
                        home.getServerId(),
                        home.getLocation(),
                        home.getState()
                );
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public <K, V, U, I> void update(HashMap<K, V> keys, U key, I value) {

    }

    public <K, V extends Integer> Set<T> findAll(K key, V value) {
        String query = String.format(
                "SELECT * FROM %s WHERE `%s`=%d",
                this.getTableName(),
                key,
                value
        );
        Set<T> homes = Sets.newConcurrentHashSet();
        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            preparedStatement.execute();

            while (resultSet.next()) {
                Home home = Home.toHome(resultSet);
                homes.add((T) home);
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
        return homes;
    }
}
