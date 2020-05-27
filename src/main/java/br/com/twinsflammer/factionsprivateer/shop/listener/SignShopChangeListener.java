package br.com.twinsflammer.factionsprivateer.shop.listener;

import br.com.twinsflammer.factionsprivateer.shop.validator.ShopValidator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author SrGutyerrez
 */
public class SignShopChangeListener implements Listener {
    @EventHandler
    public void onChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        Set<ShopValidator.Error> errors = ShopValidator.validateSignShop(event.getLines());

        if (!errors.isEmpty()) {
            String errorsMessage = errors.stream()
                    .map(ShopValidator.Error::getMessage)
                    .distinct()
                    .collect(Collectors.joining(", "));

            player.sendMessage(
                    String.format(
                            "§cNão foi possível criar a loja pelos seguintes motivos: %s",
                            errorsMessage
                    )
            );
        }
    }
}
