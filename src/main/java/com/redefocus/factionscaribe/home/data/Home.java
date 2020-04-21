package com.redefocus.factionscaribe.home.data;

import com.redefocus.api.spigot.util.serialize.LocationSerialize;
import com.redefocus.factionscaribe.home.enums.HomeState;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author oNospher
 **/
@AllArgsConstructor
@Getter
public class Home {

    private final Integer id;
    private final Integer userId;
    private final String name;

    @Setter
    private Integer serverId;

    @Setter
    private Location location;

    @Setter
    private HomeState state;

    public Boolean isPublic() {
        return state == HomeState.PUBLIC;
    }

    public Boolean isPrivate() {
        return !isPublic();
    }

    public Boolean canTeleport(CaribeUser caribeUser) {
        Chunk chunk = location.getChunk();

    //    Faction at = BoardColl.get().getFactionAt(PS.valueOf(chunk));

    //    if (at == null || at.isNone()) return true;
        return false;
    }

    public static Home toHome(ResultSet resultSet) throws SQLException {
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
