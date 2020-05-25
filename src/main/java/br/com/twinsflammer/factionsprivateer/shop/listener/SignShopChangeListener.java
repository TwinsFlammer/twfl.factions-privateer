package br.com.twinsflammer.factionsprivateer.shop.listener;

import br.com.twinsflammer.factionsprivateer.shop.data.SignShop;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * @author SrGutyerrez
 */
public class SignShopChangeListener implements Listener {
    @EventHandler
    public void onChange(SignChangeEvent event) {
        Block block = event.getBlock();

        String shopName = event.getLine(0);

        if (!shopName.equals(SignShop.SHOP_NAME)) {
            block.breakNaturally();
            return;
        }


    }
}
