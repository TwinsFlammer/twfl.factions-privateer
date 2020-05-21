package br.com.twinsflammer.factionsprivateer.economy.manager;

import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author SrGutyerrez
 */
public class EconomyManager {
    @Getter
    @Setter
    private static List<PrivateerUser> topList = Lists.newArrayList();

    public static PrivateerUser getFirstCaribeUser() {
        return EconomyManager.topList.size() > 1 ? EconomyManager.topList.get(0) : null;
    }

    public static String format(Double value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(
                Locale.forLanguageTag("pt-BR")
        );

        numberFormat.setMaximumFractionDigits(2);

        return numberFormat.format(value);
    }
}
