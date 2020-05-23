package br.com.twinsflammer.factionsprivateer.spawner.command;

import br.com.twinsflammer.api.spigot.commands.CustomCommand;
import br.com.twinsflammer.api.spigot.commands.enums.CommandRestriction;
import br.com.twinsflammer.api.spigot.util.NBTTag;
import br.com.twinsflammer.common.shared.permissions.group.GroupNames;
import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.common.shared.util.Helper;
import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.spawner.item.Spawner;
import br.com.twinsflammer.factionsprivateer.spawner.manager.SpawnerManager;
import br.com.twinsflammer.factionsprivateer.spawner.stacker.listener.SpawnerSpawnListener;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author SrGutyerrez
 */
public class SpawnerCommand extends CustomCommand {
    public SpawnerCommand() {
        super(
                "spawner",
                CommandRestriction.ALL,
                GroupNames.MASTER
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        if (args.length != 4 || !args[0].equalsIgnoreCase("give")) {
            commandSender.sendMessage("§cUtilize /spawner give <usuário> <tipo> <quantia>.");
            return;
        }

        String targetName = args[1];

        User user1 = UserManager.getUser(targetName);

        if (user1 == null) {
            commandSender.sendMessage("§cEste usuário não existe.");
            return;
        }

        PrivateerUser privateerUser = FactionsPrivateer.getInstance().getPrivateerUserFactory().getUser(targetName);

        if (privateerUser == null) {
            commandSender.sendMessage("§cEste usuário nunca entrou neste servidor.");
            return;
        }

        if (!privateerUser.isOnlineHere()) {
            commandSender.sendMessage("§cEste usuário não está online.");
            return;
        }

        try {
            EntityType entityType = EntityType.valueOf(args[2].toUpperCase());

            if (!Arrays.asList(SpawnerSpawnListener.ALLOWED_ENTITY_TYPES).contains(entityType)) {
                commandSender.sendMessage("§cEste tipo de gerador não é permitido.");
                return;
            }

            Integer amount = Helper.isInteger(args[3]) ? Integer.parseInt(args[3]) : null;

            if (amount == null || amount <= 0) {
                commandSender.sendMessage("§cVocê inseriu uma quantia inválida.");
                return;
            }

            ItemStack spawner = SpawnerManager.createSpawner(entityType, amount);

            privateerUser.giveItem(spawner);
        } catch (IllegalArgumentException exception) {
            commandSender.sendMessage("§cEste gerador não existe.");
        }
    }
}
