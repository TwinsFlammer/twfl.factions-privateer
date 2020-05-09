package com.redefocus.factionscaribe.user.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.*;
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
import com.redefocus.factionscaribe.economy.manager.EconomyManager;
import com.redefocus.factionscaribe.home.dao.HomeDao;
import com.redefocus.factionscaribe.home.data.Home;
import com.redefocus.factionscaribe.mcmmo.api.McMMoAPI;
import com.redefocus.factionscaribe.mcmmo.datatypes.player.McMMOPlayer;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.util.player.UserManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.*;
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
    protected final Integer COMBAT_DURATION = 15;
    private final String[] SCOREBOARD_LINES = {
            "§c   " + Common.SERVER_URL,
            "§4",
            "§f Cash: §c%s",
            "§f Coins: §c%s",
            "§3",
            "§f   Terras: §c%s",
            "§f   Membros: §c%s/%s",
            "§f   Poder: §c%s/%s",
            "§4 [%s] %s",
            "§2",
            "§f Poder: §c%s",
            "§f Nível: §c%s",
            "§f KDR: §c%s",
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

        this.customBoard = new CustomBoard();

        HomeDao<Home> homeDao = new HomeDao<>();

        HashMap<String, Object> keys = Maps.newHashMap();

        keys.put("user_id", this.getId());

        Set<Home> homes = homeDao.findAll(keys);

        this.homes = Lists.newArrayList(homes);

        this.skillsInventory = new CustomInventory(
                "Habilidades",
                5
        );
        this.skillsInventory.setCancelled(true);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(
                FactionsCaribe.getInstance(),
                this::updateSkillsInventory,
                0,
                20L*25
        );
    }

    public synchronized void updateSkillsInventory() {
        try {
            McMMOPlayer mcMMOPlayer = UserManager.getPlayer(this.getDisplayName());

            String firstPositionInRankName = McMMoAPI.getTopAllName(1);

            CaribeUser firstPositionInRank = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(firstPositionInRankName);

            CustomItem skull = new CustomItem(Material.SKULL_ITEM)
                    .data(3)
                    .owner(this.getDisplayName())
                    .name(this.getPrefix() + this.getDisplayName())
                    .lore(
                            "§f[" + this.HAMER_AND_PICK_CHARACTER + "] Nível total: §7" + (mcMMOPlayer == null ? 0 : mcMMOPlayer.getPowerLevel()),
                            "",
                            "§fPosição no rank: §7" + McMMoAPI.getPosition(this.getName()),
                            "§f1º colocado no rank: §7" + (firstPositionInRank == null ? "Ninguém" : firstPositionInRank.getPrefix() + firstPositionInRank.getDisplayName())
                    );

            CustomItem abilitiesRanking = new CustomItem(Material.BOOK_AND_QUILL)
                    .name("§eRank de habilidades")
                    .lore("§7Clique para abrir o Rank de Habilidades")
                    .hideAttributes()
                    .onClick(event -> {
                        Player player = (Player) event.getWhoClicked();

                        player.sendMessage("§cEm breve.");
                    });

            this.skillsInventory.setItem(12, skull);
            this.skillsInventory.setItem(14, abilitiesRanking);

            DecimalFormat decimalFormat = new DecimalFormat("###,###");

            for (DisplaySkill displaySkill : DisplaySkill.values()) {
                String skillName = displaySkill.getSkillName();

                SkillType skillType = SkillType.valueOf(skillName);
                Integer slot = displaySkill.getSlot(), data = displaySkill.getData();
                Material material = displaySkill.getMaterial();

                String firstPositionInSkillTypeRankName = McMMoAPI.getTopSkillName(skillType, 1);

                CaribeUser firstPositionInSkillTypeRank = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(firstPositionInSkillTypeRankName);

                CustomItem customItem = new CustomItem(material)
                        .data(data)
                        .name("§e" + skillType.getName())
                        .hideAttributes()
                        .lore(
                                String.format(
                                        "§fNível: §7%d/%d",
                                        mcMMOPlayer == null ? 0 : mcMMOPlayer.getSkillLevel(skillType),
                                        skillType.getMaxLevel()
                                ),
                                "",
                                "§fBônus §6VIP§f: §7" + (this.getMcMMoVIPBonus() == 1.0F ? "Nenhum" : decimalFormat.format(this.getMcMMoVIPBonus())),
                                "§fBooster: §7???",
                                "",
                                "§fPosição no rank: §7" + McMMoAPI.getPosition(skillType, this.getDisplayName()),
                                "§f1º colocado no rank: §7" + (firstPositionInSkillTypeRank == null ? "Ninguém" : firstPositionInSkillTypeRank.getPrefix() + firstPositionInSkillTypeRank.getDisplayName())
                        );

                this.skillsInventory.setItem(slot, customItem);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void setupScoreboard() {
        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(this.getName());

        CustomBoard customBoard = this.customBoard;

        Faction factionAt = this.getFactionAt();

        String factionName = this.getFactionAtName(factionAt);

        Integer[] FACTION_SCORE = {4, 5, 6, 7, 8};

        String[] SCORE_WITHOUT_FACTION = {
                "",
                "",
                this.getCash().toString(),
                EconomyManager.format(this.money),
                "",
                "",
                "",
                "",
                "",
                "§2",
                this.getPowerRounded() + "/" + this.getPowerMaxRounded(),
                mcMMOPlayer == null ? "0" : String.valueOf(mcMMOPlayer.getPowerLevel()),
                this.getKdrRounded(),
                "§1"
        };

        customBoard.title(factionName);

        for (int i = 0; i < this.SCOREBOARD_LINES.length; i++) {
            String text = this.SCOREBOARD_LINES[i];

            Boolean isFactionScore = Arrays.asList(FACTION_SCORE).contains(i);

            if (!this.hasFaction() && isFactionScore)
                continue;

            this.customBoard.set(i, String.format(
                    text,
                    isFactionScore ? this.getFactionScoreboard(i) : new String[]{SCORE_WITHOUT_FACTION[i]})
            );
        }

        customBoard.send(this.getPlayer());
    }

    private String getFactionAtName(Faction factionAt) {
        World world = this.getWorld();

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

        return factionName;
    }

    private String[] getFactionScoreboard(Integer index) {
        Faction faction = this.getFaction();

        switch (index) {
            case 8: {
                return new String[] {
                        faction.getTag(),
                        faction.getName()
                };
            }
            case 7: {
                return new String[] {
                        String.valueOf(faction.getPowerRounded()),
                        String.valueOf(faction.getPowerMaxRounded())
                };
            }
            case 6: {
                return new String[] {
                        String.valueOf(faction.getOnlinePlayers().size()),
                        String.valueOf(faction.getMembersCount())
                };
            }
            case 5: {
                return new String[] {
                        String.valueOf(faction.getLandCount())
                };
            }
        }

        return new String[] { "" };
    }

    public void updateScoreboardTitle(Faction faction) {
        this.customBoard.title(this.getFactionAtName(faction));
    }

    public void updateScoreboardTitle() {
        Faction faction = this.getFactionAt();

        this.updateScoreboardTitle(faction);
    }

    public void updateScoreboard(Integer index, Object... objects) {
        Integer[] FACTION_SCORE = {4, 5, 6, 7, 8};

        Boolean isFactionScore = Arrays.asList(FACTION_SCORE).contains(index);

        this.customBoard
                .set(
                        index,
                        String.format(
                                this.SCOREBOARD_LINES[index],
                                isFactionScore ? this.getFactionScoreboard(index) : objects
                        )
                );
    }

    public void removeScoreboard(Integer index) {
        this.customBoard.reset(index);
    }

    public void addHome(Home home) {
        this.homes.add(home);
    }

    public void setCombat(CaribeUser caribeUser) {
        String message = String.format(
                "§cVocê entrou em combate com §7%s%s§c, aguarde %d segundos para deslogar.",
                caribeUser.hasFaction() ? " [" + caribeUser.getRolePrefix() + caribeUser.getFactionTag() + "]" : "",
                caribeUser.getPrefix() + caribeUser.getDisplayName(),
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

    public Integer getGlobalChatCooldown() {
        if (this.hasGroup(GroupNames.DIRECTOR))
            return 0;
        else if (this.hasGroup(GroupNames.HELPER))
            return 2;
        else if (this.hasGroup(GroupNames.FARMER))
            return 5;

        return 15;
    }

    public Integer getLocalChatCooldown() {
        if (this.hasGroup(GroupNames.DIRECTOR))
            return 0;
        else if (this.hasGroup(GroupNames.FARMER))
            return 2;

        return 5;
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
        Chunk chunk = this.getChunk();

        return BoardColl.get().getFactionAt(PS.valueOf(chunk));
    }

    public Location getLocation() {
        Player player = this.getPlayer();

        return player.getLocation();
    }

    public World getWorld() {
        Player player = this.getPlayer();

        return player.getWorld();
    }

    public Chunk getChunk() {
        Location location = this.getLocation();

        return location.getChunk();
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

            return !caribeUser.getFaction().equals(this.getFaction())
                    && caribeUser.getFaction().getRelationWish(this.getFaction()) != Rel.ALLY;
        }

        return true;
    }

    public Boolean hasFaction() {
        Faction faction = this.getFaction();

        if (faction == null) return false;

        return !faction.getId().equals(Factions.ID_NONE)
                && !faction.getId().equals(Factions.ID_SAFEZONE)
                && !faction.getId().equals(Factions.ID_WARZONE);
    }

    public Boolean inCombat() {
        return this.combatDuration != 0L;
    }

    @RequiredArgsConstructor
    public static enum DisplaySkill {
        SWORDS(
                "SWORDS",
                20,
                0,
                Material.DIAMOND_SWORD
        ),
        ARCHERY(
                "ARCHERY",
                21,
                0,
                Material.BOW
        ),
        MINING(
                "MINING",
                22,
                0,
                Material.DIAMOND_PICKAXE
        ),
        EXCAVATION(
                "EXCAVATION",
                23,
                0,
                Material.DIAMOND_SPADE
        ),
        AXES(
                "AXES",
                24,
                0,
                Material.DIAMOND_AXE
        ),
        ACROBATICS(
                "ACROBATICS",
                30,
                0,
                Material.DIAMOND_BOOTS
        ),
        ALCHEMY(
                "ALCHEMY",
                31,
                0,
                Material.POTION
        ),
        HERBALISM(
                "HERBALISM",
                32,
                0,
                Material.SEEDS
        );

        @Getter
        private final String skillName;
        @Getter
        private final Integer slot, data;
        @Getter
        private final Material material;
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
