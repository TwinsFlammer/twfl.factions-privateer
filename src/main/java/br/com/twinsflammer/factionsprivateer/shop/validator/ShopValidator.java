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

        String preTypeAndPrice = lines[2];

        SignShop.Type type = SignShop.Type.matcher(preTypeAndPrice);

        if (type == null)
            errors.add(Error.INVALID_TYPE);

        Integer buyPrice = null, sellPrice = null;

        switch (type) {
            case BUY:
            case SELL: {
                String prePrice = preTypeAndPrice.split(type.getPrefix() + " ")[1];

                Integer price = Helper.isInteger(prePrice) ? Integer.parseInt(prePrice) : null;

                if (type == SignShop.Type.BUY)
                    buyPrice = price;
                else
                    sellPrice = price;
                break;
            }
            case BUY_AND_SELL: {
                String preBuyPrice = preTypeAndPrice.split("C.*")[1].split(" :")[0];
                String preSellPrice = preTypeAndPrice.split("V.*")[1];

                Integer price = Helper.isInteger(preBuyPrice) ? Integer.parseInt(preBuyPrice) : null;
                Integer price1 = Helper.isInteger(preSellPrice) ? Integer.parseInt(preSellPrice) :  null;

                buyPrice = price;
                sellPrice = price1;
                break;
            }
        }

        if (buyPrice == null || buyPrice <= 0 || sellPrice == null || sellPrice < 0)
            errors.add(Error.INVALID_PRICE);

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
