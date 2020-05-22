package br.com.twinsflammer.factionsprivateer.kit.data;

import br.com.twinsflammer.api.spigot.inventory.item.CustomItem;
import br.com.twinsflammer.common.shared.permissions.group.data.Group;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author SrGutyerrez
 */
@RequiredArgsConstructor
public class Kit {
    @Getter
    private final Integer id;
    @Getter
    private final String name, displayName;
    @Getter
    private final Material material;
    @Getter
    private final Integer slot;
    @Getter
    private final Category category;
    @Getter
    private final Group group;

    @Getter
    @Setter
    private List<ItemStack> items;

    @Getter
    private final Long cooldown;
    
    public CustomItem getIcon() {
        CustomItem customItem = new CustomItem(this.material)
                .name("§e" + this.displayName);

        if (!group.isDefault())
            customItem.lore(
                    String.format(
                            "§7Kit exclusivo para membros %s§7.",
                            group.getFancyPrefix()
                    )
            );

        return customItem;
    }

    @RequiredArgsConstructor
    public static enum Category {
        MAIN(
            null,
                null,
                null,
                Lists.newArrayList()
        ),
        SPECIAL(
                "Especiais",
                new CustomItem(Material.NETHER_STAR)
                .name("§6Itens especiais")
                .lore(
                        "§7Compre itens especiais para te ajudar",
                        "§7nas invasões e defesas da sua base."
                ),
                15,
                Lists.newArrayList()
        ),
        BOXES(
                "Caixas Misteriosas",
                new CustomItem(Material.ENDER_CHEST)
                .name("§3Caixas misteriosas")
                .lore(
                        "§7Compre caixas misteriosas para ganhar",
                        "§7os itens mais raros do servidor!"
                ),
                16,
                Lists.newArrayList()
        );

        @Getter
        private final String name;
        @Getter
        private final CustomItem icon;
        @Getter
        private final Integer slot;
        @Getter
        private final List<Kit> kits;
    }
}
