package com.redefocus.factionscaribe.user.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.redefocus.api.spigot.scoreboard.CustomBoard;
import com.redefocus.api.spigot.user.data.SpigotUser;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.home.dao.HomeDao;
import com.redefocus.factionscaribe.home.data.Home;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author SrGutyerrez
 */
public class CaribeUser extends SpigotUser {

    @Getter
    @Setter
    private Double money = 0.0;

    @Getter
    private final CustomBoard customBoard;

    @Setter
    private Boolean invisible = false, god = false;

    @Getter
    private final List<Integer> teleportRequests = Lists.newArrayList();

    @Getter
    private final List<Home> homes;

    public CaribeUser(User user) {
        super(user);

        this.customBoard = /*new CustomBoard()*/null;

        HomeDao<Home> homeDao = new HomeDao<>();

        HashMap<String, Object> keys = Maps.newHashMap();
        keys.put("user_id", this.getId());

        Set<Home> homes = homeDao.findAll(keys);
        this.homes = Lists.newArrayList(homes);

    }

    public World getWorld() {
        Player player = this.getPlayer();

        return player.getWorld();
    }

    public Boolean isInvisible() {
        return this.invisible;
    }

    public Boolean isGod() {
        return this.god;
    }

    public Long getTeleportTime() {
        if (this.isVIP()) return 0L;

        return TimeUnit.SECONDS.toMillis(3);
    }

    public Boolean hasHome(String name) {
        Home home = this.getHomes().stream()
                .filter(Objects::nonNull)
                .filter(home1 -> home1.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        return home != null;
    }

    public Home getHome(String name) {
        return this.getHomes().stream()
                .filter(Objects::nonNull)
                .filter(home1 -> home1.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public List<Home> getPublicHomes() {
        return this.getHomes().stream()
                .filter(Objects::nonNull)
                .filter(Home::isPublic)
                .collect(Collectors.toList());
    }

    public List<Home> getPrivateHomes() {
        return this.getHomes().stream()
                .filter(Objects::nonNull)
                .filter(Home::isPrivate)
                .collect(Collectors.toList());
    }
}
