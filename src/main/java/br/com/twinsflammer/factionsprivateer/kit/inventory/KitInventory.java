package br.com.twinsflammer.factionsprivateer.kit.inventory;

import br.com.twinsflammer.api.spigot.inventory.CustomInventory;
import br.com.twinsflammer.factionsprivateer.kit.data.Kit;

import java.util.List;

/**
 * @author SrGutyerrez
 */
public class KitInventory extends CustomInventory {
    public KitInventory() {
        super(
                "Menu de kits",
                5
        );

        this.setCancelled(true);

        List<Kit> kits = Kit.Category.MAIN.getKits();

        kits.forEach(kit -> {
            this.setItem(
                    kit.getSlot(),
                    kit.getIcon()
            );
        });

        for (Kit.Category category : Kit.Category.values()) {
            if (category == Kit.Category.MAIN) continue;

            this.setItem(
                    category.getSlot(),
                    category.getIcon()
            );
        }
    }
}
