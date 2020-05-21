package br.com.twinsflammer.factionsprivateer.antilag.data;

import br.com.twinsflammer.common.shared.util.TimeFormatter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

/**
 * @author SrGutyerrez
 */
@RequiredArgsConstructor
public class DroppedItem {
    private final static Integer DEFAULT_DESPAWN_TIME = 1;

    private final Item item;
    @Getter
    private final Long droppedTime;

    public void updateDisplayName() {
        this.item.setCustomNameVisible(true);
        this.item.setCustomName(
                String.format(
                        "§cRemovendo em %s...",
                        TimeFormatter.formatMinimized(this.getRemainingTime())
                )
        );
    }

    public void remove() {
        this.item.remove();
    }

    public Long getRemainingTime() {
        return this.droppedTime + TimeUnit.MINUTES.toMillis(DroppedItem.DEFAULT_DESPAWN_TIME);
    }

    public Boolean canBeRemoved() {
        return System.currentTimeMillis() >= this.getRemainingTime();
    }

    public Boolean isSimilar(ItemStack itemStack) {
        return this.item.getItemStack().isSimilar(itemStack);
    }
}
