package br.com.twinsflammer.factionscaribe.mcmmo.api;

//import br.com.titanwar.api.user.SpigotUser;
//import br.com.titanwar.common.shared.group.EnumGroup;
//import br.com.titanwar.common.spigot.inventory.InventoryUtils;

public class CustomMiningAPI /*implements CustomMining.McMMODrops*/ {

    //@Override
    /*public void execute(SpigotUser user, MiningDrop drop, Location location, ItemStack isDrop) {

        Player player = user.getPlayer();

        //Mining XP
        McMMOPlayer mcPlayer = UserManager.getPlayer(player.getName());
        Material ore = CustomMining.getOre(drop.getDrop());
        int mcXP = ExperienceConfig.getInstance().getXp(SkillType.MINING, ore);

        mcPlayer.applyXpGain(SkillType.MINING, mcXP, XPGainReason.PVE);

        //Double Drops
        if (user.getGroups().hasGroup(EnumGroup.LEGENDARY)
                && Config.getInstance().getDoubleDropsEnabled(SkillType.MINING, ore)) {

            if (mcPlayer.getAbilityMode(SkillType.MINING.getAbility())) {
                SkillUtils.handleDurabilityChange(player.getInventory().getItemInHand(), Config.getInstance().getAbilityToolDamage());
            }

            for (int i = mcPlayer.getAbilityMode(SkillType.MINING.getAbility()) ? 2 : 1; i != 0; i--) {
                if (SkillUtils.activationSuccessful(SecondaryAbility.MINING_DOUBLE_DROPS,
                        player, mcPlayer.getSkillLevel(SkillType.MINING), PerksUtils.handleLuckyPerks(player, SkillType.MINING))) {

                    if (CustomMining.isOre(CustomMining.getOre(isDrop.getType())) && InventoryUtils.fitItems(player.getInventory(), isDrop, -1)) {
                        InventoryUtils.addItems(player.getInventory(), isDrop, -1);
                    } else {
                        location.getWorld().dropItemNaturally(location.add(.5, .5, .5), isDrop);
                    }
                }
            }
        }

    }*/

}
