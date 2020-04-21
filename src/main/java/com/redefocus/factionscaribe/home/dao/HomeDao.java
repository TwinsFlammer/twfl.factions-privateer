package com.redefocus.factionscaribe.home.dao;

import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.api.spigot.util.serialize.LocationSerialize;
import com.redefocus.common.shared.databases.mysql.dao.Table;
import com.redefocus.factionscaribe.home.data.Home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author oNospher
 **/
public class HomeDao<T extends Home> extends Table {

    @Override
    public String getDatabaseName() {
        return SpigotAPI.getInstance().getDefaultDatabaseName("server");
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
                                "`name` VARCHAR(256) NOT NULL," +
                                "`server_id` INTEGER NOT NULL," +
                                "`location` TEXT NOT NULL," +
                                "`state` BOOLEAN NOT NULL" +
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
        ) {
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

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

}
