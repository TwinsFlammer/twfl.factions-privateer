package br.com.twinsflammer.factionsprivateer.cash.event;

import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import br.com.twinsflammer.api.shared.cash.event.ICashChangeEvent;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;

/**
 * @author SrGutyerrez
 */
public class CashChangeEvent implements ICashChangeEvent {
    @Override
    public void dispatchCashChange(User user, Integer newAmount, Integer oldAmount) {
        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());

        privateerUser.updateScoreboard(
                2,
                ""
        );
    }
}
