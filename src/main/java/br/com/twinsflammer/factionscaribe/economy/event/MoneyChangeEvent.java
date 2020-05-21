package br.com.twinsflammer.factionscaribe.economy.event;

import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import br.com.twinsflammer.api.spigot.event.TwinsEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Cancellable;

/**
 * @author SrGutyerrez
 */
@RequiredArgsConstructor
public class MoneyChangeEvent extends TwinsEvent implements Cancellable {
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
