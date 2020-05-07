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
import com.redefocus.api.spigot.inventory.CustomInventory;
import com.redefocus.api.spigot.inventory.item.CustomItem;
import com.redefocus.api.spigot.scoreboard.CustomBoard;
import com.redefocus.api.spigot.user.data.SpigotUser;
import com.redefocus.api.spigot.util.serialize.LocationSerialize;
import com.redefocus.common.shared.Common;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.server.data.Server;
import com.redefocus.common.shared.server.manager.ServerManager;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.home.dao.HomeDao;
import com.redefocus.factionscaribe.home.data.Home;
import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.util.player.UserManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author SrGutyerrez
 */
public class CaribeUser extends SpigotUser {
    public static final HashMap<SkillType, Material> SKILL_TYPES = Maps.newHashMap();

    protected final Integer COMBAT_DURATION = 15;
    private final String[] SCOREBOARD_LINES = {
            "§c  " + Common.SERVER_URL,
            "§4",
            "§f  Coins: §c%s",
            "§f  Cash: §c%s",
            "§3",
            "§f   Terras: §c%s",
            "§f   Membros: §c%s/%s",
            "§f   Poder: §c%s/%s",
            "§4  [%s] %s",
            "§2",
            "§f  KDR: §c%s",
            "§f  Nível: §c %s",
            "§1"
    };

    private final String HAMER_AND_PICK_CHARACTER = "\u2692";

    @Getter
    @Setter
    private Double money = 0.0;

    @Getter
    private final CustomBoard customBoard;

    @Getter
    @Setter
    private Long combatDuration = 0L;

    @Setter
    private Boolean invisible = false, god = false, light = false;

    @Getter
    private final List<Integer> teleportRequests = Lists.newArrayList();

    @Getter
    private final List<Home> homes;

    @Getter
    private final CustomInventory skillsInventory;

    @Getter
    @Setter
    private String lastMessage;

    @Getter
    @Setter
    private Back back;

    public CaribeUser(User user) {
        super(user);

        CaribeUser.SKILL_TYPES.put(SkillType.SWORDS, Material.DIAMOND_SWORD);
        CaribeUser.SKILL_TYPES.put(SkillType.ARCHERY, Material.BOW);
        CaribeUser.SKILL_TYPES.put(SkillType.MINING, Material.DIAMOND_PICKAXE);
        CaribeUser.SKILL_TYPES.put(SkillType.EXCAVATION, Material.DIAMOND_SPADE);
        CaribeUser.SKILL_TYPES.put(SkillType.AXES, Material.DIAMOND_AXE);
        CaribeUser.SKILL_TYPES.put(SkillType.ACROBATICS, Material.DIAMOND_BOOTS);
        CaribeUser.SKILL_TYPES.put(SkillType.ALCHEMY, Material.POTION);
        CaribeUser.SKILL_TYPES.put(SkillType.HERBALISM, Material.SEEDS);

        this.customBoard = new CustomBoard();

        HomeDao<Home> homeDao = new HomeDao<>();

        HashMap<String, Object> keys = Maps.newHashMap();

        keys.put("user_id", this.getId());

        Set<Home> homes = homeDao.findAll(keys);

        this.homes = Lists.newArrayList(homes);

        this.skillsInventory = new CustomInventory(
                "Habilidades",
                5,
                "XXXXXXXXX",
                "XXXOXOXXX",
                "XXOOOOOXX",
                "XXXOOOXXX",
                "XXXXXXXXX"
        );

        Common.getInstance().getScheduler().scheduleAtFixedRate(
                this::updateSkillsInventory,
                1,
                1,
                TimeUnit.SECONDS
        );
    }

    public synchronized void updateSkillsInventory() {
        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(this.getName());

        CustomItem skull = new CustomItem(Material.SKULL_ITEM)
                .data(3)
                .owner(this.getDisplayName())
                .name(this.getPrefix() + this.getDisplayName())
                .editable(true)
                .lore(
                        "§f["+ this.HAMER_AND_PICK_CHARACTER +"] Nível total: §7" + mcMMOPlayer.getPowerLevel(),
                        "",
                        "§fPosição no rank: §7???",
                        "§f1º colocado no rank: §7???"
                );

        CustomItem abilitiesRanking = new CustomItem(Material.BOOK_AND_QUILL)
                .name("§eRank de habilidades")
                .lore("§7Clique para abrir o Rank de Habilidades")
                .editable(true)
                .onClick(event -> {
                    Player player = (Player) event.getWhoClicked();

                    player.sendMessage("§cEm breve.");
                });

        this.skillsInventory.setCancelled(true);

        this.skillsInventory.setItem(12, skull);
        this.skillsInventory.setItem(14, abilitiesRanking);

        DecimalFormat decimalFormat = new DecimalFormat("###,###");

        Integer slot = 20;

        for (Map.Entry<SkillType, Material> entrySet : CaribeUser.SKILL_TYPES.entrySet()) {
            if (slot < 20 || slot >= 26 && slot <= 30 || slot > 33) continue;

            SkillType skillType = entrySet.getKey();
            Material material = entrySet.getValue();

            CustomItem customItem = new CustomItem(material)
                    .name("§e" + skillType.getName())
                    .editable(true)
                    .lore(
                            String.format(
                                    "§fNível: §7%d/%d",
                                    0,
                                    0
                            ),
                            "",
                            "§fBônus §6VIP§f: §7" + (this.getMcMMoVIPBonus() == 1.0F ? "Nenhum" : decimalFormat.format(this.getMcMMoVIPBonus())),
                            "§fBooster: §7???",
                            "",
                            "§fPosição no rank: §7???",
                            "§f1º colocado no rank: §7???"
                    );

            this.skillsInventory.setItem(slot, customItem);

            slot++;
        }
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

        Integer[] FACTION_SCORE = {4, 5, 6, 7, 8};

        customBoard.title(factionName);

        for (int i = 0; i <this.SCOREBOARD_LINES.length; i--) {
            String text = this.SCOREBOARD_LINES[i];

            if (!this.hasFaction() && Arrays.asList(FACTION_SCORE).contains(i))
                continue;

            this.customBoard.set(i, String.format(text, ""));
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

        if (!this.inCombat())
            this.sendMessage(message);

        this.setCombatDuration(
                System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(this.COMBAT_DURATION)
        );
    }

    public void deposit(Double value) {
        this.money += value;
    }

    public void withdraw(Double value) {
        this.money -= value;
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

    public String getKdrRounded() {
        MPlayer mPlayer = MPlayer.get(this.getUniqueId());

        return mPlayer.getKdrRounded();
    }

    public Integer getServerId() {
        Server server = this.getServer();

        return server.getId();
    }

    public Integer getHomeLimit() {
        if (this.hasGroupExact(GroupNames.NOBLE))
            return 60;
        else if (this.hasGroupExact(GroupNames.KNIGHT))
            return 40;
        else if (this.hasGroupExact(GroupNames.FARMER))
            return 20;

        return 10;
    }

    public Integer getFactionRank() {
        Faction faction = this.getFaction();

        return faction.getTopPosition();
    }

    public Integer getPowerRounded() {
        MPlayer mPlayer = MPlayer.get(this.getUniqueId());

        return mPlayer.getPowerRounded();
    }

    public Integer getPowerMaxRounded() {
        MPlayer mPlayer = MPlayer.get(this.getUniqueId());

        return mPlayer.getPowerMaxRounded();
    }

    public String getFactionAtId() {
        Faction factionAt = this.getFactionAt();

        return factionAt == null ? Factions.ID_NONE : factionAt.getId();
    }

    public Long getTeleportTime() {
        if (this.isVIP()) return 0L;

        return TimeUnit.SECONDS.toMillis(3);
    }

    public Float getMcMMoVIPBonus() {
        if (this.hasGroupExact(GroupNames.NOBLE))
            return 2.0F;
        else if (this.hasGroupExact(GroupNames.KNIGHT))
            return 1.6F;
        else if (this.hasGroupExact(GroupNames.FARMER))
            return 1.2F;

        return 1.0F;
    }

    public Faction getFaction() {
        return MPlayer.get(this.getUniqueId()).getFaction();
    }

    public Faction getFactionAt() {
        Location location = this.getLocation();

        return Faction.get(PS.valueOf(location));
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

    public Boolean isStaff() {
        return this.hasGroup(GroupNames.HELPER);
    }

    public Boolean hasHome(String name) {
        Home home = this.getHomes().stream()
                .filter(Objects::nonNull)
                .filter(home1 -> home1.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        return home != null;
    }

    public Boolean hasLight() {
        return this.light;
    }

    public Boolean canTeleport(Home home) {
        Faction faction = this.getFaction();

        Faction factionAt = Faction.get(home.getFactionId());

        return this.canTeleport(faction, factionAt);
    }

    public Boolean canTeleport(Location location) {
        Faction faction = this.getFaction();

        Faction factionAt = Faction.get(PS.valueOf(location));

        return this.canTeleport(faction, factionAt);
    }

    protected Boolean canTeleport(Faction faction, Faction factionAt) {
        if (factionAt == null || factionAt.isNone() || faction.equals(factionAt)) return true;

        if (!(faction.getRelationWish(factionAt).equals(Rel.ALLY))) return false;

        return factionAt.getPerms().get(MPerm.getPermHome()).contains(Rel.ALLY);
    }

    public Boolean canDamage(Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;

            CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(player.getUniqueId());

            if (caribeUser.getFaction().equals(this.getFaction())
                    || caribeUser.getFaction().getRelationWish(this.getFaction()) == Rel.ALLY)
                return true;

        }

        return false;
    }

    public Boolean hasFaction() {
        Faction faction = this.getFaction();

        if (faction == null) return false;

        return !faction.getId().equals(Factions.ID_NONE)
                && !faction.getId().equals(Factions.ID_SAFEZONE)
                && !faction.getId().equals(Factions.ID_WARZONE);
    }

    public Boolean inCombat() {
        return this.combatDuration >= System.currentTimeMillis() && this.combatDuration != 0L;
    }

    @RequiredArgsConstructor
    public static class Back {
        @Getter
        private final Integer serverId;
        private final String serializedLocation;

        public Server getServer() {
            return ServerManager.getServer(this.serverId);
        }

        public Location getLocation() {
            return LocationSerialize.toLocation(this.serializedLocation);
        }
    }
}
