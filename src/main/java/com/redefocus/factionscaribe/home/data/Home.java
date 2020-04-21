package com.redefocus.factionscaribe.home.data;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.redefocus.api.spigot.util.serialize.LocationSerialize;
import com.redefocus.factionscaribe.home.enums.HomeState;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.var;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

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

        MPlayer mPlayer = MPlayer.get(caribeUser.getUniqueId());
        Faction at = BoardColl.get().getFactionAt(PS.valueOf(chunk));

        if (at == null || at.isNone()) return true;
        return isAllowed(mPlayer.getFaction(), at);
    }

    private Boolean isAllowed(Faction faction, Faction at) {
        if(faction.equals(at)) return true;
        if(!(faction.getRelationWish(at) == Rel.ALLY)) return false;
        return at.getPerms().get(MPerm.getPermHome()).contains(Rel.ALLY);
    }

    public static Home toHome(ResultSet resultSet) throws SQLException {
        String preLocation = resultSet.getString("location");
        Location location = LocationSerialize.toLocation(preLocation);
        String preState = resultSet.getString("state");
        HomeState state = HomeState.valueOf(preState);
        return new Home(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getInt("server_id"),
                location,
                state
        );
    }
}
