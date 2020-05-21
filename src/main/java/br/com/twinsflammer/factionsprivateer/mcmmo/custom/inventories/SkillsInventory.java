package br.com.twinsflammer.factionsprivateer.mcmmo.custom.inventories;

//import br.com.titanwar.common.spigot.inventory.CustomInventory;
//import br.com.titanwar.common.spigot.utils.ItemCustom;
//import br.com.titanwar.server.Server;
//import br.com.titanwar.server.user.ServerUser;


public class SkillsInventory /*extends CustomInventory implements Runnable*/ {
/*
    private static final String TITLE = "Habilidades";
    private static final int MAX_LEVEL = 1000;

    private final ServerUser user;
    private PlayerProfile mcMMOProfile;
    private final HashMap<SkillType, Integer> skills;
    private BukkitTask task;

    @Setter
    private boolean availableSkills = false;

    public SkillsInventory(ServerUser user) {
        super(5 * 9, TITLE);

        this.user = user;
        McMMOPlayer mcPlayer = UserManager.getOfflinePlayer(user.getProfile().getName());
        this.mcMMOProfile = mcPlayer != null ? mcPlayer.getProfile() : null;
        this.skills = Maps.newHashMap(McMMORanksTask.getPlayerRank(user.getProfile().getName()));
    }\

    @Override
    public void run() {
        if (this.mcMMOProfile == null) {
            McMMOPlayer mcPlayer = UserManager.getOfflinePlayer(user.getProfile().getName());
            this.mcMMOProfile = mcPlayer != null ? mcPlayer.getProfile() : null;

            if (this.mcMMOProfile == null) {
                this.mcMMOProfile = mcMMO.getDatabaseManager().loadPlayerProfile(user.getProfile().getName(), user.getProfile().getUniqueId(), false);

                if (this.mcMMOProfile == null) {
                    return;
                }
            }
        }

        this.skills.clear();
        this.skills.putAll(Maps.newHashMap(McMMORanksTask.getPlayerRank(user.getProfile().getName())));

        this.setItem(12, getTotalLevelIcon());
        this.setItem(14, getRankInventoryIcon(), (event) -> event.getWhoClicked().openInventory(this.user.getTopSkillsInventory()));

        if (availableSkills) {

            int index = 19;

            for (SkillType type : SkillType.values()) {

                if (type.isActive()) {
                    if ((index + 1) % 9 == 0) {
                        index += 2;
                    }

                    this.setItem(index++, type);
                }

            }

            return;
        }

        // 20 - 24 e 29 - 33
        this.setItem(20, SkillType.SWORDS);
        this.setItem(21, SkillType.ARCHERY);
        this.setItem(22, SkillType.MINING);
        this.setItem(23, SkillType.EXCAVATION);
        this.setItem(24, SkillType.AXES);

        //this.setItem(29, SkillType.REPAIR);
        this.setItem(30, SkillType.ACROBATICS);
        this.setItem(31, SkillType.ALCHEMY);
        this.setItem(32, SkillType.HERBALISM);
        //this.setItem(33, SkillType.UNARMED);
        //this.setItem(34, SkillType.TAMING);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(Server.getInstance(), this, 0L, 20L);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (task != null) {
            Bukkit.getScheduler().cancelTask(this.task.getTaskId());
        }
        this.task = null;
    }

    private void setItem(int i, SkillType skillType) {
        if (skillType.isActive()) {
            this.setItem(i, getSkillIcon(skillType));
        }
    }

    private ItemStack getSkillIcon(SkillType skillType) {

        ItemCustom itemCustom = new ItemCustom(McMMOUtils.getSkillItemStack(skillType));
        itemCustom.meta()
                .setDisplayName("&e" + skillType.getName())
                .setLore("&fNível: &7" + mcMMOProfile.getSkillLevel(skillType) + "/" + MAX_LEVEL)
                .addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);

        if (!skillType.isChildSkill()) {

            Integer rank = this.skills.get(skillType);
            PlayerStat top = McMMORanksTask.getTopSkill(skillType);

            itemCustom.meta().addLore(
                    "",
                    //"&fBônus &6VIP&f: &7" + (this.user.getGroups().hasGroup(EnumGroup.VIP) ? "+" + BoosterManager.getVIPMultiplier(this.user) + "x" : "Nenhum."),
                    //"&fBooster: &7" + (this.user.getBoosters().hasBooster(skillType) ? "+2x ("
                    //+ Cooldowns.getFormattedTimeLeft(user.getBoosters().getBooster(skillType).getDuration()) + ")" : "Nenhum."),
                    "",
                    "&fPosição no rank: &7" + (rank == null ? "Indefinido." : rank + "º"),
                    "&f1º colocado no rank: &7" + (top == null ? "Indefinido." : top.name)
            );

            if (skillType.equals(SkillType.TAMING)) {
                itemCustom.meta().addLore("", "&eApenas jogadores &6VIP&e podem ", "&eevoluir esta habilidade.");
            }

        }

        return itemCustom.build();
    }

    private ItemStack getTotalLevelIcon() {

        ItemCustom itemCustom = new ItemCustom(Material.SKULL_ITEM).setDurability(3).meta().setSkullOwner(user.getPlayer().getName()).item();
        Integer rank = this.skills.get(null);
        PlayerStat top = McMMORanksTask.getTopSkill(null);

        itemCustom.meta().setDisplayName(this.user.getDisplayName())
                .setLore("&f[" + SkillType.POWER_LEVEL_ICON + "] Nível total: &7" + getPowerLevel())
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addLore(
                        "",
                        "&fPosição no rank: &7" + (rank == null ? "Indefinido." : rank + "º"),
                        "&f1º colocado no rank: &7" + (top == null ? "Indefinido." : top.name)
                );

        return itemCustom.build();
    }

    private ItemStack getRankInventoryIcon() {

        ItemCustom itemCustom = new ItemCustom(Material.BOOK_AND_QUILL);

        itemCustom.meta()
                .setDisplayName(ChatColor.YELLOW + "Rank de Habilidades")
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addLore("&7Clique para abrir o Rank de Habilidades.");

        return itemCustom.build();
    }

    public int getPowerLevel() {
        int powerLevel = 0;

        for (SkillType type : SkillType.NON_CHILD_SKILLS()) {
            if (user.getPlayer() != null ? type.getPermissions(user.getPlayer()) : true) {
                powerLevel += mcMMOProfile.getSkillLevel(type);
            }
        }

        return powerLevel;
    }

 */
}
