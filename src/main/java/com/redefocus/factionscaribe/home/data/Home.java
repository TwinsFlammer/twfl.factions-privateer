package com.redefocus.factionscaribe.home.data;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

/**
 * @author oNospher
 **/
@AllArgsConstructor
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



    enum State {
        PUBLIC,
        PRIVATE;
    }
}
