package br.com.twinsflammer.factionscaribe.kit.inventory;

import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import br.com.twinsflammer.api.spigot.inventory.CustomPaginateInventory;
import br.com.twinsflammer.api.spigot.inventory.item.CustomItem;
import br.com.twinsflammer.factionscaribe.kit.data.Kit;

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
