package com.redefocus.factionscaribe.spawner.item;

import com.redefocus.api.spigot.inventory.item.CustomItem;
import com.redefocus.factionscaribe.util.EntityUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;

/**
 * @author SrGutyerrez
 */
public class Spawner extends CustomItem {
    public Spawner(EntityType entityType) {
        super(Material.MOB_SPAWNER);

        this.name("§eGerador de monstros");
        this.enchant(Enchantment.DURABILITY, 1);
        this.hideAttributes();
        this.lore(
                String.format(
                        "§7Tipo: §f%s",
                        EntityUtil.translate(entityType.getName())
                )
        );
    }
}
