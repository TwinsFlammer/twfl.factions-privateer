package com.redefocus.factionscaribe.home.manager;

import com.redefocus.api.spigot.util.serialize.LocationSerialize;
import com.redefocus.factionscaribe.home.data.Home;
import com.redefocus.factionscaribe.home.enums.HomeState;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author oNospher
 **/
public class HomeManager {

    public Home toHome(ResultSet resultSet) throws SQLException {
        return new Home(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getInt("server_id"),
                LocationSerialize.toLocation(resultSet.getString("location")),
                HomeState.valueOf(resultSet.getString("state"))
        );
    }
}
