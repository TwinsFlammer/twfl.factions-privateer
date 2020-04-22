package com.redefocus.factionscaribe.home.dao;

import com.google.common.collect.Sets;
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
                                "`state` VARCHAR(12) NOT NULL" +
                                ");",
                        this.getTableName()
                )
        );
    }

    public T insert(T home) {
        String query = String.format(
                "INSERT INTO %s " +
                        "(" +
                        "`user_id`," +
                        "`name`," +
                        "`server_id`," +
                        "`location`," +
                        "`state`" +
                        ")" +
                        " VALUES " +
                        "(" +
                        "%d," +
                        "'%s'," +
                        "%d," +
                        "'%s'," +
                        "'%s'" +
                        ");",
                this.getTableName(),
                home.getUserId(),
                home.getName(),
                home.getServerId(),
                LocationSerialize.toString(home.getLocation()),
                home.getState().toString()
        );
        System.out.println(query);
        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.execute();

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("user_id", home.getUserId());
            hashMap.put("name", home.getName());

            return this.findOne(hashMap);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public <K, V, U, I> void update(HashMap<K, V> keys, U key, I value) {
        String where = this.generateWhere(keys);

        String query = String.format(
                "UPDATE %s SET %s WHERE `%s`=%s",
                this.getTableName(),
                where,
                key,
                value
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public <K, V> T findOne(HashMap<K, V> keys) {
        String where = this.generateParameters(keys).replaceAll(",", " AND");

        String query = String.format(
                "SELECT * FROM %s WHERE %s;",
                this.getTableName(),
                where
        );

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return (T) Home.toHome(resultSet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public <K, V, U, I, T> Set<T> findAll(HashMap<K, V> keys) {
        String query = String.format(
                "SELECT * FROM %s WHERE %s;",
                this.getTableName(),
                this.generateParameters(keys)
        );

        Set<T> homes = Sets.newConcurrentHashSet();

        try (
                Connection connection = this.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Home home = Home.toHome(resultSet);

                homes.add((T) home);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return homes;
    }
}
