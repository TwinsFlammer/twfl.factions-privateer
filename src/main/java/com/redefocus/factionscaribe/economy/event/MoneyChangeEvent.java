package com.redefocus.factionscaribe.economy.event;

import com.redefocus.api.spigot.event.CommunityEvent;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Cancellable;

/**
 * @author SrGutyerrez
 */
@RequiredArgsConstructor
public class MoneyChangeEvent extends CommunityEvent implements Cancellable {
    @Getter
    private final CaribeUser caribeUser;
    @Getter
    private final Double oldAmount, newAmount;

    private Boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
