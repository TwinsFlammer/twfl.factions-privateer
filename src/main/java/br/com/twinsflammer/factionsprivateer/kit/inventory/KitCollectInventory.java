package br.com.twinsflammer.factionsprivateer.kit.inventory;

import br.com.twinsflammer.api.spigot.inventory.CustomPaginateInventory;
import br.com.twinsflammer.api.spigot.inventory.item.CustomItem;
import br.com.twinsflammer.common.shared.util.TimeFormatter;
import br.com.twinsflammer.factionsprivateer.kit.data.Kit;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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

        Integer collectItemSlot = 5 * 9 - 4;

        if (privateerUser.hasGroupExact(kit.getGroup())) {
            if (privateerUser.hasCollectedKit(kit)) {
                this.setItem(
                        collectItemSlot,
                        new CustomItem(Material.MINECART)
                                .name(
                                        String.format(
                                                "§cAguarde %s para coletar este kit novamente",
                                                TimeFormatter.formatMinimized(
                                                        privateerUser.getTimeToCollectKitAgain(kit) - System.currentTimeMillis()
                                                )
                                        )
                                )
                );
            } else {
                this.setItem(
                        collectItemSlot,
                        new CustomItem(Material.STORAGE_MINECART)
                                .name("§aClique para coletar")
                                .onClick(event -> {
                                    Player player = (Player) event.getWhoClicked();

                                    if (privateerUser.getInventorySpace() < kit.getItems().size()) {
                                        player.sendMessage("§cVocê não possui espaço no inventário para coletar este kit.");
                                        return;
                                    }

                                    privateerUser.collectKit(kit);

                                    player.closeInventory();
                                })
                );
            }
        } else {
            this.setItem(
                    collectItemSlot,
                    new CustomItem(Material.BARRIER)
                            .name("§cVocê não pode coletar este kit.")
            );
        }
    }
}
