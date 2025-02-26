package br.com.twinsflammer.factionsprivateer.user.data;

import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.inventory.CustomInventory;
import br.com.twinsflammer.api.spigot.inventory.item.CustomItem;
import br.com.twinsflammer.api.spigot.scoreboard.CustomBoard;
import br.com.twinsflammer.api.spigot.user.data.SpigotUser;
import br.com.twinsflammer.api.spigot.util.serialize.InventorySerialize;
import br.com.twinsflammer.api.spigot.util.serialize.ItemSerialize;
import br.com.twinsflammer.api.spigot.util.serialize.LocationSerialize;
import br.com.twinsflammer.common.shared.Common;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.server.data.Server;
import br.com.twinsflammer.common.shared.server.manager.ServerManager;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.commands.player.tpa.data.TpaRequest;
import br.com.twinsflammer.factionsprivateer.economy.manager.EconomyManager;
import br.com.twinsflammer.factionsprivateer.home.dao.HomeDao;
import br.com.twinsflammer.factionsprivateer.home.data.Home;
import br.com.twinsflammer.factionsprivateer.kit.data.Kit;
import br.com.twinsflammer.factionsprivateer.mcmmo.api.McMMoAPI;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.PlayerProfile;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.mcMMO;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionsprivateer.user.item.channel.ItemChannel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author SrGutyerrez
 */
public class PrivateerUser extends SpigotUser {
    public static final String ENDER_CHEST_TITLE = "Báu do fim";

    protected final Integer COMBAT_DURATION = 15;
    private final String[] SCOREBOARD_LINES = {
            "§c  " + Common.SERVER_URL,
            "§4",
            "§f Cash: §6%s",
            "§f Coins: §c%s",
            "§3",
            "§f   Terras: §c%s",
            "§f   Membros: §c%s/%s",
            "§f   Poder: §c%s/%s",
            "§4 [%s] %s",
            "§7  Nenhuma facção",
            "§2",
            "§f Poder: §c%s",
            "§f Nível: §c%s",
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
    private final List<TpaRequest> teleportRequestsSent = Lists.newArrayList(),
            teleportRequestsReceived = Lists.newArrayList();

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

    @Setter
    private Long lastTpaTime = 0L, lastEnderPearlTeleportTime = 0L;

    private final HashMap<SkillType, Integer> skills = Maps.newHashMap();

    private final HashMap<Integer, Long> collectedKits = Maps.newHashMap();

    @Getter
    @Setter
    private Inventory seeingInventory, enderChest;

    public PrivateerUser(User user) {
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

        Common.getInstance().getScheduler().scheduleAtFixedRate(
                () -> {
                    for (DisplaySkill displaySkill : DisplaySkill.values()) {
                        String skillName = displaySkill.getSkillName();

                        SkillType skillType = SkillType.valueOf(skillName);
                        Integer value = McMMoAPI.getPosition(skillType, this.getName());

                        this.skills.put(skillType, value);
                    }
                },
                0,
                5,
                TimeUnit.MINUTES
        );

        Bukkit.getScheduler().scheduleSyncRepeatingTask(
                FactionsPrivateer.getInstance(),
                this::updateSkillsInventory,
                0,
                1200
        );
    }

    public synchronized void updateSkillsInventory() {
        try {
            McMMOPlayer mcMMOPlayer = UserManager.getPlayer(this.getDisplayName());

            PlayerProfile firstPositionInRankProfile = mcMMO.getPlayerProfiles().get(0);

            PrivateerUser firstPositionInRank = firstPositionInRankProfile == null ? null : FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(firstPositionInRankProfile.getUniqueId());

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

                PlayerProfile firstPositionInSkillTypeRankProfile = mcMMO.getTopSkillsPlayerProfile().get(skillType);

                PrivateerUser firstPositionInSkillTypeRank = firstPositionInSkillTypeRankProfile == null ? null : FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(firstPositionInSkillTypeRankProfile.getUniqueId());

                Integer position = this.skills.get(skillType);

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
                                "§fPosição no rank: §7" + (position == null ? "Indefinido." : position + "º"),
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

        Integer[] FACTION_SCORE = {5, 6, 7, 8};

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
                "",
                "§2",
                this.getPowerRounded() + "/" + this.getPowerMaxRounded(),
                mcMMOPlayer == null ? "0" : String.valueOf(mcMMOPlayer.getPowerLevel()),
                "§1"
        };

        customBoard.title(factionName);

        for (int i = 0; i < this.SCOREBOARD_LINES.length; i++) {
            String text = this.SCOREBOARD_LINES[i];

            Boolean isFactionScore = Arrays.asList(FACTION_SCORE).contains(i);

            if ((!this.hasFaction() && isFactionScore) || (i == 9 && this.hasFaction()))
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

        String factionName = "§c§lTWINS FLAMMER";

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
                return new String[]{
                        faction.getTag(),
                        faction.getName()
                };
            }
            case 7: {
                return new String[]{
                        String.valueOf(faction.getPowerRounded()),
                        String.valueOf(faction.getPowerMaxRounded())
                };
            }
            case 6: {
                return new String[]{
                        String.valueOf(faction.getOnlinePlayers().size()),
                        String.valueOf(faction.getMembersCount())
                };
            }
            case 5: {
                return new String[]{
                        String.valueOf(faction.getLandCount())
                };
            }
        }

        return new String[]{""};
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

    public void setCombat(PrivateerUser privateerUser) {
        String message = String.format(
                "§cVocê entrou em combate com §7%s%s§c, aguarde %d segundos para deslogar.",
                privateerUser.hasFaction() ? "[" + privateerUser.getRolePrefix() + privateerUser.getFactionTag() + "] " : "",
                privateerUser.getPrefix() + privateerUser.getDisplayName(),
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

    public void sendTpaRequest(PrivateerUser privateerUser) {
        TpaRequest tpaRequest = new TpaRequest(
                UUID.randomUUID(),
                this.getId(),
                privateerUser.getId(),
                SpigotAPI.getRootServerId(),
                TpaRequest.Action.SEND,
                System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1)
        );

        tpaRequest.publish();
    }

    public void denyTpaRequest(TpaRequest tpaRequest) {
        tpaRequest.setAction(TpaRequest.Action.DENY);

        tpaRequest.publish();
    }

    public void acceptTpaRequest(TpaRequest tpaRequest) {
        tpaRequest.setAction(TpaRequest.Action.ACCEPT);

        tpaRequest.publish();
    }

    public void cancelTpaRequest(TpaRequest tpaRequest) {
        tpaRequest.setAction(TpaRequest.Action.CANCEL);

        tpaRequest.publish();
    }

    public void removeTpaRequest(TpaRequest tpaRequest) {
        this.getTeleportRequestsReceived().removeIf(tpaRequest1 ->
                tpaRequest.getUuid().equals(tpaRequest1.getUuid()));
        this.getTeleportRequestsSent().removeIf(tpaRequest1 ->
                tpaRequest.getUuid().equals(tpaRequest1.getUuid()));
    }

    public void removeExpiredTpaRequests() {
        this.teleportRequestsReceived.removeIf(TpaRequest::hasExpired);
        this.teleportRequestsSent.removeIf(TpaRequest::hasExpired);
    }

    public void setInventory(Inventory inventory, ItemStack... armor) {
        JSONObject serializedInventory = InventorySerialize.toJsonObject(inventory);

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("inventory", serializedInventory);

        if (armor != null) {
            jsonObject.put("armor", ItemSerialize.toBase64List(
                    Arrays.asList(armor)
            ));
        }

        try (Jedis jedis = this.getRedis().getJedisPool().getResource()) {
            jedis.hset(
                    "player_inventory",
                    this.getId().toString(),
                    jsonObject.toString()
            );
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }
    }

    public void giveItem(ItemStack itemStack) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("user_id", this.getId());
        jsonObject.put("item", ItemSerialize.toBase64(itemStack));

        ItemChannel itemChannel = new ItemChannel();

        itemChannel.sendMessage(
                jsonObject.toString()
        );
    }

    public void toggleAllMods() {
        this.toggleVisibility();

        this.toggleLightMode();
    }

    public void toggleVisibility() {
        Player player = this.getPlayer();

        Bukkit.getOnlinePlayers().forEach(player1 -> {
            PrivateerUser privateerUser1 = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player1.getUniqueId());

            if (this.isStaff()) {
                if (this.isInvisible() && !privateerUser1.isStaff())
                    player1.hidePlayer(player);
                else player1.showPlayer(player);
            }

            if (privateerUser1.isStaff()) {
                if (privateerUser1.isInvisible() && !this.isStaff())
                    player.hidePlayer(player1);
                else player.showPlayer(player1);
            }
        });
    }

    public void toggleLightMode() {
        Player player = this.getPlayer();

        if (this.hasLight())
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.NIGHT_VISION,
                    99999,
                    0
            ));
        else {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }

    public void collectKit(Kit kit) {
        Player player = this.getPlayer();

        kit.getItems().forEach(player.getInventory()::addItem);

        this.collectedKits.put(kit.getId(), System.currentTimeMillis());
    }

    public void removeInventory() {
        try (Jedis jedis = this.getRedis().getJedisPool().getResource()) {
            jedis.hdel("player_inventory", this.getId().toString());
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }
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

    public Integer getInventorySpace() {
        Inventory playerInventory = this.getInventory();

        Integer space = 0;

        for (int i = 0; i < playerInventory.getSize(); i++)
            if (playerInventory.getItem(i) == null || playerInventory.getItem(i).getType() == Material.AIR) {
                space++;
            }

        return space;
    }

    public Integer getEnderChestRows() {
        if (this.hasGroupExact(GroupNames.NOBLE))
            return 6;
        else if (this.hasGroupExact(GroupNames.KNIGHT))
            return 5;
        else if (this.hasGroupExact(GroupNames.FARMER))
            return 4;

        return 3;
    }

    public String getFactionAtId() {
        Faction factionAt = this.getFactionAt();

        return factionAt == null ? Factions.ID_NONE : factionAt.getId();
    }

    public Long getTeleportTime() {
        if (this.isVIP()) return 0L;

        return System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(3);
    }

    public Long getTimeToSendTpaAgain() {
        return this.lastTpaTime + TimeUnit.SECONDS.toMillis(30);
    }

    public Long getLastEnderPearlTeleportTime() {
        return this.lastEnderPearlTeleportTime + TimeUnit.SECONDS.toMillis(15);
    }

    public Long getTimeToCollectKitAgain(Kit kit) {
        return this.collectedKits.get(kit.getId()) + kit.getCooldown();
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

    public Inventory getInventory() {
        try (Jedis jedis = this.getRedis().getJedisPool().getResource()) {
            if (!jedis.hexists("player_inventory", this.getId().toString()))
                return null;

            String serializedPlayerInventory = jedis.hget(
                    "player_inventory",
                    this.getId().toString()
            );

            JSONObject jsonObject = (JSONObject) JSONValue.parse(serializedPlayerInventory);

            JSONObject serializedInventory = (JSONObject) jsonObject.get("inventory");

            return InventorySerialize.toInventory(serializedInventory);
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public ItemStack[] getArmorContents() {
        try (Jedis jedis = this.getRedis().getJedisPool().getResource()) {
            if (!jedis.hexists("player_inventory", this.getId().toString()))
                return null;

            String serializedPlayerInventory = jedis.hget(
                    "player_inventory",
                    this.getId().toString()
            );

            JSONObject jsonObject = (JSONObject) JSONValue.parse(serializedPlayerInventory);

            String serializedItems = (String) jsonObject.get("armor");

            if (serializedItems == null) return null;

            List<ItemStack> armorContents = ItemSerialize.fromBase64List(serializedItems);

            ItemStack[] armor = new ItemStack[4];

            ItemStack helmet = armorContents.stream()
                    .filter(itemStack -> itemStack.getType().name().contains("_HELMET"))
                    .findFirst()
                    .orElse(null);

            armor[3] = helmet;

            ItemStack chestPlate = armorContents.stream()
                    .filter(itemStack -> itemStack.getType().name().contains("_CHESTPLATE"))
                    .findFirst()
                    .orElse(null);

            armor[2] = chestPlate;

            ItemStack leggings = armorContents.stream()
                    .filter(itemStack -> itemStack.getType().name().contains("_LEGGINGS"))
                    .findFirst()
                    .orElse(null);

            armor[1] = leggings;

            ItemStack boots = armorContents.stream()
                    .filter(itemStack -> itemStack.getType().name().contains("_BOOTS"))
                    .findFirst()
                    .orElse(null);

            armor[0] = boots;

            return armor;
        } catch (JedisDataException exception) {
            exception.printStackTrace();
        }

        return null;
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

    @Deprecated
    public Boolean isGod() {
        return this.isInGodMode();
    }

    public Boolean isStaff() {
        return this.hasGroup(GroupNames.HELPER);
    }

    public Boolean isOnlineHere() {
        return this.isOnline() && SpigotAPI.getSubServersId().contains(this.getServerId());
    }

    public Boolean isInGodMode() {
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

            PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

            if (privateerUser.isGod()) return false;

            if (!privateerUser.hasFaction()) return true;

            return !privateerUser.getFaction().equals(this.getFaction())
                    && privateerUser.getFaction().getRelationWish(this.getFaction()) != Rel.ALLY;
        }

        return true;
    }

    public Boolean canSendTpaAgain() {
        return System.currentTimeMillis() > this.getTimeToSendTpaAgain();
    }

    public Boolean canUseEnderPearlListener() {
        return System.currentTimeMillis() > this.getLastEnderPearlTeleportTime();
    }

    public Boolean hasFaction() {
        Faction faction = this.getFaction();

        if (faction == null) return false;

        return !faction.getId().equals(Factions.ID_NONE)
                && !faction.getId().equals(Factions.ID_SAFEZONE)
                && !faction.getId().equals(Factions.ID_WARZONE);
    }

    public Boolean hasCollectedKit(Kit kit) {
        return this.collectedKits.containsKey(kit.getId());
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
