package com.redefocus.factionscaribe.cash.event;

import com.redefocus.api.shared.cash.event.ICashChangeEvent;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.factionscaribe.FactionsCaribe;
import com.redefocus.factionscaribe.user.data.CaribeUser;

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
