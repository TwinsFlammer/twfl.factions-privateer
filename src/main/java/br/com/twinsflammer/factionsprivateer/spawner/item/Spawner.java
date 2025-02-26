package br.com.twinsflammer.factionsprivateer.spawner.item;

import br.com.twinsflammer.api.spigot.inventory.item.CustomItem;
import br.com.twinsflammer.factionsprivateer.util.EntityUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;

/**
 * @author SrGutyerrez
 */
public class Spawner extends CustomItem {
    public static final String NBT_TAG = "ENTITY_TYPE";

    public Spawner(EntityType entityType) {
        super(Material.MOB_SPAWNER);

        this.name("§eGerador de monstros");
        this.enchant(
                Enchantment.DURABILITY,
                1
        );
        this.hideAttributes();
        this.lore(
                String.format(
                        "§7Tipo: §f%s",
                        EntityUtil.translate(entityType.getName())
                )
        );
    }
}
