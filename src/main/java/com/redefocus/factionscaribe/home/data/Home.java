package com.redefocus.factionscaribe.home.data;

import com.redefocus.factionscaribe.home.enums.HomeState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

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
}
