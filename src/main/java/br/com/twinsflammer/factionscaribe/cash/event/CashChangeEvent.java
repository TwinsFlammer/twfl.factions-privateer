package br.com.twinsflammer.factionscaribe.cash.event;

import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
import br.com.twinsflammer.api.shared.cash.event.ICashChangeEvent;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.factionscaribe.FactionsCaribe;

/**
 * @author SrGutyerrez
 */
public class CashChangeEvent implements ICashChangeEvent {
    @Override
    public void dispatchCashChange(User user, Integer newAmount, Integer oldAmount) {
        CaribeUser caribeUser = FactionsCaribe.getInstance().getCaribeUserFactory().getUser(user.getId());

        caribeUser.updateScoreboard(
                2,
                ""
        );
    }
}
