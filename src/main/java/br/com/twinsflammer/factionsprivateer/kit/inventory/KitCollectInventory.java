package br.com.twinsflammer.factionsprivateer.kit.inventory;

import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.api.spigot.inventory.CustomPaginateInventory;
import br.com.twinsflammer.api.spigot.inventory.item.CustomItem;
import br.com.twinsflammer.factionsprivateer.kit.data.Kit;

/**
 * @author SrGutyerrez
 */
public class KitCollectInventory extends CustomPaginateInventory {
    public KitCollectInventory(Kit kit, PrivateerUser privateerUser) {
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

        if (privateerUser.hasCollectedKit(kit)) {

        }
    }
}
