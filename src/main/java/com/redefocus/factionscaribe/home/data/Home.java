package com.redefocus.factionscaribe.home.data;

import com.redefocus.api.spigot.util.serialize.LocationSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
    private State state;

    public Boolean isPublic() {
        return state == State.PUBLIC;
    }

    public Boolean isPrivate() {
        return !isPublic();
    }

   /* public Boolean canTeleport(CaribeUser caribeUser) {
        Chunk chunk = location.getChunk();

        MPlayer mPlayer = MPlayer.get(caribeUser.getUniqueId());
        Faction at = BoardColl.get().getFactionAt(PS.valueOf(chunk));

        return isAllowed(mPlayer.getFaction(), at);
    }

    private Boolean isAllowed(Faction faction, Faction at) {
        if (at == null || at.isNone()) return true;
        if(faction.equals(at)) return true;
        if(!(faction.getRelationWish(at) == Rel.ALLY)) return false;
        return at.getPerms().get(MPerm.getPermHome()).contains(Rel.ALLY);
    } */

    public static Home toHome(ResultSet resultSet) throws SQLException {
        String preLocation = resultSet.getString("location");
        Location location = LocationSerialize.toLocation(preLocation);
        String preState = resultSet.getString("state");
        State state = State.valueOf(preState);
        return new Home(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getInt("server_id"),
                location,
                state
        );
    }

    public enum State {
        PUBLIC,
        PRIVATE
    }
}
