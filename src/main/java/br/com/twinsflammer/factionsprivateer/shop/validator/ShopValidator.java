package br.com.twinsflammer.factionsprivateer.shop.validator;

import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.factionsprivateer.shop.data.SignShop;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * @author SrGutyerrez
 */
public class ShopValidator {
    public static Set<Error> validateSignShop(String... lines) {
        Set<Error> errors = Sets.newConcurrentHashSet();

        String shopName = lines[0];

        if (!shopName.equals(SignShop.SHOP_NAME))
            errors.add(Error.INVALID_NAME);

        Integer amount = Helper.isInteger(lines[1]) ? Integer.parseInt(lines[1]) : null;

        if (amount == null || amount < 1 || amount > 2304)
            errors.add(Error.INVALID_AMOUNT);

        String prePrice = lines[2];

        SignShop.Type type = SignShop.Type.matcher(prePrice);

        if (type == null)
            errors.add(Error.INVALID_TYPE);

        return errors;
    }

    @RequiredArgsConstructor
    public enum Error {
        INVALID_NAME(
                "O nome da loja está inválido."
        ),
        INVALID_AMOUNT(
                "A quantia inseria está inválida."
        ),
        INVALID_TYPE(
                "O tipo de loja é inválido."
        ),
        INVALID_PRICE(
                "O preço da loja está inválido."
        );

        @Getter
        private final String message;
    }
}
