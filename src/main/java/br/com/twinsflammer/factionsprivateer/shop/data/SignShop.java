package br.com.twinsflammer.factionsprivateer.shop.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Pattern;

/**
 * @author SrGutyerrez
 */
@AllArgsConstructor
@Setter
public class SignShop {
    public static final String SHOP_NAME = "Loja";

    @Getter
    private final Location location;

    @Getter
    private ItemStack itemStack;

    @Getter
    private Integer buyPrice, sellPrice, amount;

    private Boolean active;

    @Getter
    private String name;

    public void update() {
        Sign sign = this.getSign();

        Material material = this.itemStack == null ? Material.AIR : this.itemStack.getType();
        short durability = this.itemStack == null ? 0 : this.itemStack.getDurability();

        sign.setLine(1, this.amount.toString());
        sign.setLine(2, this.getSignPriceString());
        sign.setLine(3, this.name == null ? material.toString() + ":" + durability : this.name);
    }

    private String getSignPriceString() {
        Type type = this.getType();

        switch (type) {
            case BUY:
            case SELL: {
                return type.getPrefix() + " " + this.sellPrice;
            }
            case BUY_AND_SELL: {
                return String.format(
                        "%s : %s",
                        "C " + this.buyPrice,
                        "V " + this.sellPrice
                );
            }
            default:
                return null;
        }
    }

    public Sign getSign() {
        Block block = this.location.getBlock();

        return (Sign) block.getState();
    }

    public Type getType() {
        Sign sign = this.getSign();

        String line1 = sign.getLine(2);

        if (line1.contains("C") && line1.contains("V"))
            return Type.BUY_AND_SELL;
        else if (line1.contains("C"))
            return Type.BUY;
        else if (line1.contains("V"))
            return Type.SELL;
        else
            return null;
    }

    public Boolean isActive() {
        return this.active;
    }

    @RequiredArgsConstructor
    public enum Type {
        BUY(
                'C',
                Pattern.compile("C.*")
        ),
        SELL(
                'V',
                Pattern.compile("V.*")
        ),
        BUY_AND_SELL(
                null,
                Pattern.compile("C.* : V.*")
        );

        @Getter
        private final Character prefix;
        @Getter
        private final Pattern pattern;

        public static Type matcher(String line) {
            for (Type type : Type.values()) {
                if (type.getPattern().matcher(line).matches())
                    return type;
            }

            return null;
        }
    }
}
