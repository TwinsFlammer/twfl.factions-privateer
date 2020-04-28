package com.redefocus.factionscaribe.mcmmo.custom.inventories;

//import br.com.titanwar.common.spigot.inventory.CustomInventory;
//import static br.com.titanwar.common.spigot.inventory.ICustomInventory.BACK_ARROW;
//import br.com.titanwar.common.spigot.utils.ItemCustom;
//import br.com.titanwar.server.Server;
//import br.com.titanwar.server.user.ServerUser;


public class TopSkillsInventory /*extends CustomInventory implements Runnable*/ {
/*
    private static final String INVENTORY_TITLE = "Rank de Habilidades";
    private BukkitTask task;
    @Setter
    private boolean availableSkills = false;

    public TopSkillsInventory(ServerUser user) {
        super(6 * 9, INVENTORY_TITLE);

        this.setItem(49, BACK_ARROW, event -> event.getWhoClicked().openInventory(user.getSkillsInventory()));
    }

    @Override
    public void run() {

        setItem(13, getTotalLevelIcon());

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
        setItem(20, SkillType.SWORDS);
        setItem(21, SkillType.ARCHERY);
        setItem(22, SkillType.MINING);
        setItem(23, SkillType.EXCAVATION);
        setItem(24, SkillType.AXES);

        //setItem(29, SkillType.REPAIR);
        setItem(30, SkillType.ACROBATICS);
        setItem(31, SkillType.ALCHEMY);
        setItem(32, SkillType.HERBALISM);
        //setItem(33, SkillType.UNARMED);
        //setItem(34, SkillType.TAMING);

    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(Server.getInstance(), this, 0L, 20L * 60);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (this.task != null) {
            Bukkit.getScheduler().cancelTask(this.task.getTaskId());
        }

        this.task = null;
    }

    private void setItem(int i, SkillType skillType) {
        if (skillType.isActive()) {
            setItem(i, getSkillIcon(skillType));
        }
    }

    private ItemStack getSkillIcon(SkillType skillType) {

        ItemCustom itemCustom = new ItemCustom(McMMOUtils.getSkillItemStack(skillType));
        itemCustom.meta()
                .setDisplayName("&e" + skillType.getName())
                .addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);

        if (!skillType.isChildSkill()) {

            List<String> ranking = Lists.newArrayList();

            for (int i = 0; i < 10; i++) {
                PlayerStat rank = McMMORanksTask.getTopSkill(skillType, i);
                ranking.add("&f" + (i + 1) + "º: &7" + (rank == null ? "Indefinido." : rank.name + "&8 - &7" + rank.statVal));
            }

            itemCustom.meta().addLore(ranking);

            if (skillType.equals(SkillType.TAMING)) {
                itemCustom.meta().addLore("", "&eApenas jogadores &6VIP&e podem ", "&eevoluir esta habilidade.");
            }
        }

        return itemCustom.build();
    }

    private ItemStack getTotalLevelIcon() {

        ItemCustom itemCustom = new ItemCustom(Material.BOOK_AND_QUILL);
        List<String> ranking = Lists.newArrayList();

        for (int i = 0; i < 10; i++) {
            PlayerStat rank = McMMORanksTask.getTopSkill(null, i);
            ranking.add("&f" + (i + 1) + "º: &7" + (rank == null ? "Indefinido." : rank.name + "&8 - &7" + rank.statVal));
        }

        return itemCustom.meta()
                .setDisplayName(ChatColor.YELLOW + "Nível Total").addFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addLore("Representa a soma de todas as habilidades.", "")
                .addLore(ranking)
                .build();

    }
*/
}
