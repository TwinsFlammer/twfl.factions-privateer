package com.redefocus.factionscaribe.kit.inventory;

import com.redefocus.api.spigot.inventory.CustomPaginateInventory;
import com.redefocus.api.spigot.inventory.item.CustomItem;
import com.redefocus.factionscaribe.kit.data.Kit;
import com.redefocus.factionscaribe.user.data.CaribeUser;

/**
 * @author SrGutyerrez
 */
public class KitCollectInventory extends CustomPaginateInventory {
    public KitCollectInventory(Kit kit, CaribeUser caribeUser) {
        super(
                "Kit - " + kit.getDisplayName(),
                5,
                "XXXXXXXXX",
                "XXOOOOOXX",
                "XXOOOOOXX",
                "XXOOOOOXX",
                "XXXXXXXXX"
        );

        this.setCancelled(true);

        kit.getItems().forEach(itemStack -> {
            this.addItem(new CustomItem(itemStack));
        });

        if (caribeUser.hasCollectedKit(kit)) {

        }
    }
}
