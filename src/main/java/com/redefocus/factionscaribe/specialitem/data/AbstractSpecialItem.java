package com.redefocus.factionscaribe.specialitem.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

/**
 * @author SrGutyerrez
 */
@RequiredArgsConstructor
public abstract class AbstractSpecialItem<E extends Event> {
    @Getter
    private final String name;

    public abstract ItemStack getItemStack();

    public abstract BiConsumer<E, E> getEventConsumer();

    public abstract Class<? extends Event> getEventType();
}
