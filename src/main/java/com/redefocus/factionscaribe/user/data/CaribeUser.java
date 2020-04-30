package com.redefocus.factionscaribe.user.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.redefocus.api.spigot.SpigotAPI;
import com.redefocus.api.spigot.scoreboard.CustomBoard;
import com.redefocus.api.spigot.user.data.SpigotUser;
import com.redefocus.common.shared.Common;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.server.data.Server;
import com.redefocus.factionscaribe.home.dao.HomeDao;
import com.redefocus.factionscaribe.home.data.Home;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author SrGutyerrez
 */
public class CaribeUser extends SpigotUser {
    protected final Integer COMBAT_DURATION = 15;
    private final String[] SCOREBOARD_LINES = {
            "§1",
            "§f  KDR: §c%d",
            "§f  Nível: §c %s",
            "§2",
            "§4  [%s] %s",
            "§f   Poder: §c%d/%d",
            "§f   Membros: §c%d/%d",
            "§f   Terras: §c%d",
            "§3",
            "§f  Coins: §c%s",
            "§f  Cash: §c%s",
            Common.SERVER_URL
    };

    @Getter
    @Setter
    private Double money = 0.0;

    @Getter
    private final CustomBoard customBoard;

    @Getter
    @Setter
    private Long combatDuration;

    @Setter
    private Boolean invisible = false, god = false;

    @Getter
    private final List<Integer> teleportRequests = Lists.newArrayList();

    @Getter
    private final List<Home> homes;

    public CaribeUser(User user) {
        super(user);

        this.customBoard = new CustomBoard();

        HomeDao<Home> homeDao = new HomeDao<>();

        HashMap<String, Object> keys = Maps.newHashMap();

        keys.put("user_id", this.getId());

        Set<Home> homes = homeDao.findAll(keys);

        this.homes = Lists.newArrayList(homes);
    }

    public void setupScoreboard() {
        CustomBoard customBoard = this.customBoard;

        Location location = this.getLocation();
        World world = this.getWorld();

        Faction factionAt = Faction.get(PS.valueOf(location));

        String factionName = "§c§lREDE FOCUS";

        if (factionAt != null)
            switch (factionAt.getId()) {
                case Factions.ID_NONE: {
                    factionName = "§aÁrea livre";
                    break;
                }
                case Factions.ID_WARZONE: {
                    factionName = "§cZona de guerra";
                    break;
                }
                case Factions.ID_SAFEZONE: {
                    factionName = "§aÁrea protegida";
                    break;
                }
                default: {
                    factionName = world.getName().equalsIgnoreCase("caribe_mine") ? "§7Mundo de mineração" : String.format(
                            "§7%s - %s",
                            factionAt.getTag(),
                            factionAt.getName()
                    );
                    break;
                }
            }

        Integer[] FACTION_SCORE = { 4, 5, 6, 7, 8 };

        customBoard.title(factionName);

        for (int i = 0; i < this.SCOREBOARD_LINES.length; i++) {
            String text = this.SCOREBOARD_LINES[i];

            if (!this.hasFaction() && Arrays.asList(FACTION_SCORE).contains(i))
                continue;

            this.customBoard.set(i, String.format(text));
        }

        customBoard.send(this.getPlayer());
    }

    public void updateScoreboard(Integer index, Object... objects) {
        this.customBoard
                .set(
                        index,
                        String.format(
                                this.SCOREBOARD_LINES[index],
                                objects
                        )
                );
    }

    public void addHome(Home home) {
        this.homes.add(home);
    }

    public void setCombat(CaribeUser caribeUser) {
        String message = String.format(
                "§cVocê entrou em combate com §7%s§c, aguarde %d segundos para deslogar.",
                caribeUser.getFactionTag() + caribeUser.getName(),
                this.COMBAT_DURATION
        );

        this.setCombatDuration(
                System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(this.COMBAT_DURATION)
        );

        if(!this.inCombat())
            this.sendMessage(message);
    }

    public String getRolePrefix() {
        MPlayer mPlayer = MPlayer.get(this.getUniqueId());

        return mPlayer.getRole().getPrefix();
    }

    public String getFactionName() {
        return this.hasFaction() ? this.getFaction().getName() : "";
    }

    public String getFactionTag() {
        return this.hasFaction() ? this.getFaction().getTag() : "";
    }

    public String getFancyFaction() {
        return this.hasFaction() ? "[" + this.getFactionTag() + "] " + this.getFactionName() : "";
    }

    public Integer getServerId() {
        Server server = this.getServer();

        return server.getId();
    }

    public long getCombatTime() {
        return this.combatDuration - System.currentTimeMillis();
    }

    public Faction getFaction() {
        return MPlayer.get(this.getUniqueId()).getFaction();
    }

    public Long getTeleportTime() {
        if (this.isVIP()) return 0L;

        return TimeUnit.SECONDS.toMillis(3);
    }

    public Location getLocation() {
        Player player = this.getPlayer();

        return player.getLocation();
    }

    public World getWorld() {
        Player player = this.getPlayer();

        return player.getWorld();
    }

    public Home getHomeExact(String name) {
        return this.homes.stream()
                .filter(Objects::nonNull)
                .filter(home1 -> home1.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Home getHome(String name) {
        return this.homes.stream()
                .filter(Objects::nonNull)
                .filter(home1 -> home1.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> new Home(
                        0,
                        this.getId(),
                        name,
                        SpigotAPI.getRootServerId(),
                        null,
                        null,
                        Home.State.PRIVATE
                ));
    }

    public List<Home> getPublicHomes() {
        return this.homes.stream()
                .filter(Objects::nonNull)
                .filter(Home::isPublic)
                .collect(Collectors.toList());
    }

    public List<Home> getPrivateHomes() {
        return this.homes.stream()
                .filter(Objects::nonNull)
                .filter(Home::isPrivate)
                .collect(Collectors.toList());
    }

    public Boolean isInvisible() {
        return this.invisible;
    }

    public Boolean isGod() {
        return this.god;
    }

    public Boolean hasHome(String name) {
        Home home = this.getHomes().stream()
                .filter(Objects::nonNull)
                .filter(home1 -> home1.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        return home != null;
    }

    public Boolean canTeleport(Home home) {
        MPlayer mPlayer = MPlayer.get(this.getUniqueId());

        Faction faction = mPlayer.getFaction();
        Faction factionAt = Faction.get(home.getFactionId());

        if (factionAt == null || factionAt.isNone() || faction.equals(factionAt)) return true;

        if (!(faction.getRelationWish(factionAt).equals(Rel.ALLY))) return false;

        return factionAt.getPerms().get(MPerm.getPermHome()).contains(Rel.ALLY);
    }

    public Boolean hasFaction() {
        MPlayer mPlayer = MPlayer.get(this.getUniqueId());
        
        return mPlayer.hasFaction();
    }

    public Boolean inCombat() {
        return this.combatDuration >= System.currentTimeMillis() && this.combatDuration != 0L;
    }
}
