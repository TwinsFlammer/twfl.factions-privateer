package com.redefocus.factionscaribe.kit.inventory;

import com.redefocus.api.spigot.inventory.CustomInventory;
import com.redefocus.factionscaribe.kit.data.Kit;

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
