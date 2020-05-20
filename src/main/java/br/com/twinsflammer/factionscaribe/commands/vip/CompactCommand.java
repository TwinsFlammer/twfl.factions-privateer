package br.com.twinsflammer.factionscaribe.commands.vip;

import com.google.common.collect.Maps;
import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SrGutyerrez
 */
public class CompactCommand extends CustomCommand {
    private final HashMap<Material, Material> COMPACT_MATERIALS;

    public CompactCommand() {
        super(
                "compactar",
                CommandRestriction.IN_GAME,
                GroupNames.FARMER
        );

        this.COMPACT_MATERIALS = Maps.newHashMap();

        this.COMPACT_MATERIALS.put(Material.IRON_INGOT, Material.IRON_BLOCK);
        this.COMPACT_MATERIALS.put(Material.EMERALD, Material.EMERALD_BLOCK);
        this.COMPACT_MATERIALS.put(Material.DIAMOND, Material.DIAMOND_BLOCK);
        this.COMPACT_MATERIALS.put(Material.SLIME_BALL, Material.SLIME_BLOCK);
        this.COMPACT_MATERIALS.put(Material.GOLD_INGOT, Material.GOLD_BLOCK);
        this.COMPACT_MATERIALS.put(Material.GOLD_NUGGET, Material.GOLD_INGOT);
        this.COMPACT_MATERIALS.put(Material.REDSTONE, Material.REDSTONE_BLOCK);
        this.COMPACT_MATERIALS.put(Material.GLOWSTONE_DUST, Material.GLOWSTONE);
        this.COMPACT_MATERIALS.put(Material.INK_SACK, Material.LAPIS_BLOCK);
        this.COMPACT_MATERIALS.put(Material.COAL, Material.COAL_BLOCK);
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        Player player = (Player) commandSender;

        HashMap<Material, Integer> materialsAmount = Maps.newHashMap();

        for (ItemStack content : player.getInventory().getContents()) {
            if (content == null || content.getType() == Material.AIR || content.getAmount() < 9) continue;

            if (this.COMPACT_MATERIALS.containsKey(content.getType())) {
                Material material = content.getType();

                if (materialsAmount.containsKey(material))
                    materialsAmount.put(material, (materialsAmount.get(material) + content.getAmount()));
                else materialsAmount.put(material, content.getAmount());

                player.getInventory().remove(content);
            }
        }

        Integer items = 0;

        for (Map.Entry<Material, Integer> entry : materialsAmount.entrySet()) {
            Material material = entry.getKey();
            Integer amount = entry.getValue();

            if (amount / 9 <= 0) continue;

            Integer returns = amount % 9;

            if (returns > 0) {
                ItemStack item1 = new ItemStack(material);
                if (item1.getType() == Material.INK_SACK) item1.setDurability((short) 4);
                item1.setAmount(returns);
                System.out.println(item1.getType());
                player.getInventory().addItem(item1);
            }

            Material newMaterial = this.COMPACT_MATERIALS.get(material);

            Integer amount1 = (amount / 9);

            ItemStack item = new ItemStack(newMaterial);

            item.setAmount(amount1);

            player.getInventory().addItem(item);

            items += amount;
        }

        if (items == 0) {
            commandSender.sendMessage("§cNão haviam itens para serem compactados.");
            return;
        }

        commandSender.sendMessage("§a" + (items / 9) + " coisas foram transformados em outras coisas.");
    }
}
