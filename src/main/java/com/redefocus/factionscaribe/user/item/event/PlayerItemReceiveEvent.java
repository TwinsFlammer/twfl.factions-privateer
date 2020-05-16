package com.redefocus.factionscaribe.user.item.event;

import com.redefocus.api.spigot.event.FocusEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author SrGutyerrez
 */
@RequiredArgsConstructor
@Getter
public class PlayerItemReceiveEvent extends FocusEvent {
    private final Player player;
    private final ItemStack itemStack;
}
