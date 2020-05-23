package br.com.twinsflammer.factionsprivateer.spawner.listener;

import br.com.twinsflammer.api.spigot.SpigotAPI;
import br.com.twinsflammer.api.spigot.util.NBTTag;
import br.com.twinsflammer.api.spigot.util.jsontext.data.JSONText;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.spawner.item.Spawner;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author SrGutyerrez
 */
public class SpawnerPlaceListener implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();

        Block block = event.getBlock();

        if (block.getType() != Material.MOB_SPAWNER) return;

        ItemStack itemStack = player.getItemInHand();

        NBTTagCompound nbtTagCompound = NBTTag.getNBTTag(itemStack);

        if (!nbtTagCompound.hasKey(Spawner.NBT_TAG)) {
            event.setCancelled(true);

            player.sendMessage("§cEste não é um spawner válido, alertamos os membros da equipe.");

            PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(player.getUniqueId());

            JSONText jsonText = new JSONText()
                    .text("\n")
                    .next()
                    .text("§cO usuário " + privateerUser.getPrefix() + privateerUser.getDisplayName() + "§c tentou colocar um spawner inválido no chão.")
                    .next()
                    .text("§cClique ")
                    .next()
                    .text("§a§lAQUI")
                    .execute("/tp " + privateerUser.getDisplayName())
                    .next()
                    .text("§c para ir até ele.")
                    .next();

            SpigotAPI.getUsers()
                    .stream()
                    .filter(user -> user.hasGroup(GroupNames.ADMINISTRATOR))
                    .forEach(user -> {
                        PrivateerUser privateerUser1 = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(user.getId());

                        privateerUser1.sendMessage(jsonText);
                    });
            return;
        }

        String preEntityType = nbtTagCompound.getString(Spawner.NBT_TAG);
        EntityType entityType = EntityType.valueOf(preEntityType);

        CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();

        creatureSpawner.setSpawnedType(entityType);
    }
}
