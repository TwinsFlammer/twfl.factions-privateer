package br.com.twinsflammer.factionsprivateer.home.data;

import br.com.twinsflammer.api.spigot.util.serialize.LocationSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.World;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

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
    private String factionId;

    @Setter
    private Location location;

    @Setter
    private State state;

    public String getFancyLocation() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");

        Double x = this.location.getX(), y = this.location.getY(), z = this.location.getZ();

        return String.format(
                "x:%s y:%s z:%s",
                decimalFormat.format(x),
                decimalFormat.format(y),
                decimalFormat.format(z)
        );
    }

    public String getWorldName() {
        Location location = this.location;

        World world = location.getWorld();

        return world.getName();
    }

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
                resultSet.getString("faction_id"),
                location,
                state
        );
    }

    public Boolean isPublic() {
        return state == State.PUBLIC;
    }

    public Boolean isPrivate() {
        return !isPublic();
    }

    @RequiredArgsConstructor
    public enum State {
        PUBLIC(
                "publica"
        ),
        PRIVATE(
                "particular"
        );

        @Getter
        private final String name;
    }
}
