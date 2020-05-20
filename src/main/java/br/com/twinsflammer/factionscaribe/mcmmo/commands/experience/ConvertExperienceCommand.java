package br.com.twinsflammer.factionscaribe.mcmmo.commands.experience;

import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.experience.FormulaType;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.mcMMO;
import br.com.twinsflammer.factionscaribe.mcmmo.runnables.database.FormulaConversionTask;
import br.com.twinsflammer.factionscaribe.mcmmo.runnables.player.PlayerProfileLoadingTask;
import br.com.twinsflammer.factionscaribe.mcmmo.util.player.UserManager;
import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConvertExperienceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 2:
                FormulaType previousType = mcMMO.getFormulaManager().getPreviousFormulaType();
                FormulaType newType = FormulaType.getFormulaType(args[1].toUpperCase());

                if (newType == FormulaType.UNKNOWN) {
                    sender.sendMessage(LocaleLoader.getString("Commands.mcconvert.Experience.Invalid"));
                    return true;
                }

                if (previousType == newType) {
                    sender.sendMessage(LocaleLoader.getString("Commands.mcconvert.Experience.Same", newType.toString()));
                    return true;
                }

                sender.sendMessage(LocaleLoader.getString("Commands.mcconvert.Experience.Start", previousType.toString(), newType.toString()));

                UserManager.saveAll();
                UserManager.clearAll();

                new FormulaConversionTask(sender, newType).runTaskLater(FactionsCaribe.getInstance(), 1);

                for (Player player : FactionsCaribe.getInstance().getServer().getOnlinePlayers()) {
                    new PlayerProfileLoadingTask(player).runTaskLaterAsynchronously(FactionsCaribe.getInstance(), 1); // 1 Tick delay to ensure the player is marked as online before we begin loading
                }

                return true;

            default:
                return false;
        }
    }
}
