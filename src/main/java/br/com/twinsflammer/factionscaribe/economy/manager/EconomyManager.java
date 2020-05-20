package br.com.twinsflammer.factionscaribe.economy.manager;

import br.com.twinsflammer.factionscaribe.user.data.CaribeUser;
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
    private static List<CaribeUser> topList = Lists.newArrayList();

    public static CaribeUser getFirstCaribeUser() {
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
